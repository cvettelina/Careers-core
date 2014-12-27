package core.application.validations;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import api.application.request.User;

@Stateless
public class UserValidation {
    
    @PersistenceContext
    EntityManager em;
    
    @EJB
    CommonValidation commonValidation;
    
    public void validate(User request){
        commonValidation.validateNotNull(request, "request");
        commonValidation.validateName(request.getUsername(), "username");
    }

}
