package core.authentication;

import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.annotations.interception.ServerInterceptor;
import org.jboss.resteasy.core.Headers;
import org.jboss.resteasy.core.ResourceMethodInvoker;
import org.jboss.resteasy.core.ServerResponse;
import org.jboss.resteasy.logging.impl.Log4jLogger;

import org.jboss.resteasy.util.Base64;

import core.application.entities.CredentialsEntity;
import core.application.entities.UserEntity;

@Provider
@ServerInterceptor
public class SecurityInterceptor implements ContainerRequestFilter {
    private static final String AUTHORIZATION_PROPERTY = "Authorization";
    private static final String AUTHENTICATION_SCHEME = "Basic";
    private static final ServerResponse MUST_RESET_PASSWORD = new ServerResponse("Please reset your password", 401, new Headers<Object>());
    private static final ServerResponse ACCESS_DENIED = new ServerResponse("Access denied for this resource", 401, new Headers<Object>());
    private static final ServerResponse ACCESS_FORBIDDEN = new ServerResponse("Nobody can access this resource", 403, new Headers<Object>());
    private static final ServerResponse SERVER_ERROR = new ServerResponse("INTERNAL SERVER ERROR", 500, new Headers<Object>());
    private static final String URL = "jdbc:mysql://localhost:3306/application?characterEncoding=UTF-8";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "369258";

    private Log4jLogger log = new Log4jLogger(SecurityInterceptor.class.getCanonicalName());

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        ResourceMethodInvoker methodInvoker = (ResourceMethodInvoker) requestContext.getProperty("org.jboss.resteasy.core.ResourceMethodInvoker");
        Method method = methodInvoker.getMethod();

        // Access allowed for all
        if (method.isAnnotationPresent(PermitAll.class)) {
            return;
        }
        // Access denied for all
        if (method.isAnnotationPresent(DenyAll.class)) {
            requestContext.abortWith(ACCESS_FORBIDDEN);
            return;
        }

        // Fetch authorization header
        String authorization = requestContext.getHeaderString(AUTHORIZATION_PROPERTY);

        // If no authorization information present; block access
        if (authorization == null || authorization.isEmpty()) {
            requestContext.abortWith(ACCESS_DENIED);
            return;
        }

        // Get token
        final String encodedToken = authorization.replaceFirst(AUTHENTICATION_SCHEME + " ", "");

        // Decode token
        String token;
        try {
            token = new String(Base64.decode(encodedToken));
        } catch (IOException e) {
            requestContext.abortWith(SERVER_ERROR);
            return;
        }

        CredentialsEntity entity = getUserEnity(token);
        if (entity == null) {
            requestContext.abortWith(ACCESS_DENIED);
            return;
        }

        if (entity.getUser().getActive() != 1 && entity.getUser().getPassword() != null) {
            requestContext.abortWith(ACCESS_DENIED);
            return;
        }

        if (entity.getUser().getActive() == 0 && entity.getUser().getPassword() == null && !"changePassword".equals(method.getName())) {
            requestContext.abortWith(MUST_RESET_PASSWORD);
            return;
        }

        Date now = new Date();
        Long lastActivity = (now.getTime() - entity.getLoginTime().getTime()) / (1000 * 60);
        if (lastActivity > 15) {
            requestContext.abortWith(ACCESS_DENIED);
            return;
        }

        // Verify user access
        if (method.isAnnotationPresent(RolesAllowed.class)) {
            RolesAllowed rolesAnnotation = method.getAnnotation(RolesAllowed.class);
            Set<String> rolesSet = new HashSet<String>(Arrays.asList(rolesAnnotation.value()));

            // Is user valid?
            if (!isUserAllowed(entity.getUser(), rolesSet)) {
                requestContext.abortWith(ACCESS_DENIED);
                return;
            }

            updateLoginTime(token);
        }
    }

    private boolean isUserAllowed(UserEntity user, Set<String> rolesSet) {
        boolean isAllowed = false;

        String userRole = user.getType();

        // Step 2. Verify user role
        if (rolesSet.contains(userRole)) {
            isAllowed = true;
        }

        return isAllowed;
    }

    private CredentialsEntity getUserEnity(String token) {
        CredentialsEntity credentialsEntity = new CredentialsEntity();
        Long userId = null;
        Connection connection = null; // manages connection
        PreparedStatement findUser = null;
        ResultSet result;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            findUser = connection.prepareStatement("SELECT token, user_id, login_time FROM credentials WHERE token='" + token + "';");
            result = findUser.executeQuery();
            if (result.next()) {
                credentialsEntity.setLoginTime(result.getTimestamp("login_time"));
                userId = result.getLong("user_id");
            } else {
                return null;
            }
            findUser = connection.prepareStatement("SELECT password, type, active FROM user WHERE id=" + userId + ";");
            result = findUser.executeQuery();
            if (result.next()) {
                UserEntity user = new UserEntity();
                user.setType(result.getString("type"));
                user.setId(userId);
                user.setActive(result.getInt("active"));
                user.setPassword(result.getString("password"));
                credentialsEntity.setUser(user);
            }
        } catch (Exception ex) {
            log.error("Error while getting user data from DB", ex);
            return null;
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                log.error("Error while getting user data from DB", e);
            }
        }
        return credentialsEntity;
    }

    private void updateLoginTime(String token) {
        Connection connection = null; // manages connection
        PreparedStatement findUser = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            Date now = new Date();
            findUser = connection.prepareStatement("UPDATE credentials SET login_time = '" + new Timestamp(now.getTime()) + "' WHERE token='" + token
                    + "';");
            findUser.executeUpdate();
        } catch (Exception ex) {
            log.error("Error while updating user data from DB", ex);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                log.error("Error while updating user data from DB", e);
            }
        }
    }

}
