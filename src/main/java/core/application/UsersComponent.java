package core.application;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jboss.resteasy.logging.impl.Log4jLogger;

import core.application.builders.EntityBuilder;
import core.application.entities.UserEntity;
import core.application.exception.ApplicationException;
import core.application.exception.ExceptionType;
import core.application.util.Hash;
import api.application.request.ChangePasswordRequest;
import api.application.request.User;

@Stateless
public class UsersComponent {

    private Log4jLogger log = new Log4jLogger(UsersComponent.class.getCanonicalName());

    @PersistenceContext
    EntityManager em;

    public void createUser(User request) {
        EntityBuilder builder = new EntityBuilder();
        UserEntity entity = builder.buildUserEntity(request);
        em.persist(entity);
    }

    public void changePassword(Long userId, ChangePasswordRequest request) {
        UserEntity user = getUserEntityById(userId);
        if (user == null) {
            throw new ApplicationException(ExceptionType.UNKNOWN_USER);
        }
        String currentPass = user.getPassword();
        String oldPassword = null;
        String newPass = null;
        try {
            if (currentPass != null && !"".equals(currentPass)) {
                oldPassword = Hash.createHash(request.getOldPassword());
                if (Hash.validatePassword(oldPassword, currentPass)) {
                    throw new ApplicationException(ExceptionType.INVALID_PARAMETER, "oldPassword");
                }
            }
            newPass = Hash.createHash(request.getNewPassword());
        } catch (NoSuchAlgorithmException e) {
            log.error("Errow while changing password", e);
            throw new InternalError();
        } catch (InvalidKeySpecException e) {
            log.error("Errow while changing password", e);
            throw new InternalError();
        }
        user.setPassword(newPass);
        user.setLastUpdated(new Date());
        user.setActive(1);
        em.merge(user);
    }

    public void changeStatus(Integer newStatus, Long userId) {
        UserEntity user = getUserEntityById(userId);
        if (user == null) {
            throw new ApplicationException(ExceptionType.UNKNOWN_USER);
        }
        user.setActive(newStatus);
        user.setLastUpdated(new Date());
        em.merge(user);
    }

    public UserEntity getUserEntityByName(String username) {
        Query query = em.createNamedQuery(UserEntity.GET_BY_NAME);
        query.setParameter("username", username);
        query.setMaxResults(1);
        try {
            return (UserEntity) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public UserEntity getUserEntityById(Long userId) {
        UserEntity entity = em.find(UserEntity.class, userId);
        return entity;
    }

}
