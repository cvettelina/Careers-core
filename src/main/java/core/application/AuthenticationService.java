package core.application;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;

import org.jboss.resteasy.util.Base64;

import core.application.entities.CredentialsEntity;
import core.application.entities.UserEntity;
import core.application.exception.ApplicationException;
import core.application.exception.ExceptionType;
import core.application.util.Hash;

import api.application.Authentication;
import api.application.request.Credentials;
import api.application.response.LoginResponse;

@Stateless
public class AuthenticationService implements Authentication {

    @PersistenceContext
    EntityManager em;

    @EJB
    UsersComponent usersComponent;

    @SuppressWarnings("unchecked")
    @Override
    public LoginResponse login(Credentials credentials, HttpServletRequest httpRequest) {
        validateCredentials(credentials);
        UserEntity userEntity = usersComponent.getUserEntityByName(credentials.getUsername());
        if (userEntity == null) {
            throw new ApplicationException(ExceptionType.UNKNOWN_USER);
        }

        if (userEntity.getPassword() != null && !"".equals(userEntity.getPassword())) {
            try {
                if (!Hash.validatePassword(credentials.getPassword(), userEntity.getPassword())) {
                    throw new ApplicationException(ExceptionType.INVALID_PARAMETER, "password");
                }
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                throw new ApplicationException(ExceptionType.INTERNAL_ERROR);
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
                throw new ApplicationException(ExceptionType.INTERNAL_ERROR);
            }

        }
        Query query = em.createNamedQuery(CredentialsEntity.GET_BY_USER);
        query.setParameter("userId", userEntity.getId());
        query.setMaxResults(1);
        List<CredentialsEntity> result = query.getResultList();
        if (result != null && result.size() > 0) {
            CredentialsEntity oldCredentials = result.get(0);
            if (oldCredentials != null) {
                Date now = new Date();
                Long lastActivity = (now.getTime() - oldCredentials.getLoginTime().getTime()) / (1000 * 60);
                if (lastActivity < 15) {
                    LoginResponse response = new LoginResponse();
                    response.setToken(Base64.encodeBytes(oldCredentials.getToken().getBytes()));
                    response.setUserId(userEntity.getId());
                    oldCredentials.setLoginTime(new Timestamp(now.getTime()));
                    em.merge(oldCredentials);
                    return response;
                }
            }
        }

        SecureRandom rand = new SecureRandom();
        String token = (new BigInteger(130, rand).toString(32));

        Date dateNow = new Date();
        em.persist(new CredentialsEntity(token, userEntity, new Timestamp(dateNow.getTime())));
        LoginResponse response = new LoginResponse();
        response.setToken(Base64.encodeBytes(token.getBytes()));
        response.setUserId(userEntity.getId());
        return response;
    }
    
    private void validateCredentials(Credentials credentials) {
        if(credentials == null) {
            throw new ApplicationException(ExceptionType.MISSING_PARAMETER, "credentials");
        }
        if(credentials.getUsername() == null) {
            throw new ApplicationException(ExceptionType.MISSING_PARAMETER, "username");
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void logout(Long userId) {
        Query query = em.createNamedQuery(CredentialsEntity.GET_BY_USER);
        query.setParameter("userId", userId);
        List<CredentialsEntity> results = query.getResultList();
        if (results != null && !results.isEmpty()) {
            for (CredentialsEntity entity : results) {
                em.remove(entity);
            }
        }
    }

}
