package core.application.validations;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import core.application.entities.UserEntity;
import core.application.exception.ApplicationException;
import core.application.exception.ExceptionType;

import api.application.request.User;

@Stateless
public class UserValidation {

    @PersistenceContext
    EntityManager em;

    @EJB
    CommonValidation commonValidation;

    public void validate(User request) {
        commonValidation.validateNotNull(request, "request");
        commonValidation.validateName(request.getUsername(), "username");
        commonValidation.validateEmail(request.getEmail());
        commonValidation.validateNotNull(request.getType(), "type");

    }

    public void checkIfUserExists(String username) {
        Query query = em.createNamedQuery(UserEntity.GET_BY_NAME);
        query.setParameter("username", username);
        if (!query.getResultList().isEmpty()) {
            throw new ApplicationException(ExceptionType.INVALID_PARAMETER, "username");
        }
    }

    public void checkIfUserEmailExists(String email) {
        Query query = em.createNamedQuery(UserEntity.GET_BY_EMAIL);
        query.setParameter("email", email);
        if (!query.getResultList().isEmpty()) {
            throw new ApplicationException(ExceptionType.INVALID_PARAMETER, "email");
        }
    }

}
