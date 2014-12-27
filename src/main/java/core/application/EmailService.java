package core.application;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import core.application.entities.PersonEntity;
import core.application.exception.ApplicationException;
import core.application.exception.ExceptionType;
import core.application.validations.CommonValidation;

import api.application.Emails;
import api.application.request.CustomEmail;

@Stateless
public class EmailService implements Emails {

    @PersistenceContext
    EntityManager em;

    @EJB
    EmailsComponent component;

    @EJB
    CommonValidation commonValidation;

    @Override
    public void sendTeplateEmail(Long recipientId) {
        commonValidation.validateNotNull(recipientId, "recipientId");
        PersonEntity person = em.find(PersonEntity.class, recipientId);
        if (person == null) {
            throw new ApplicationException(ExceptionType.UNKNOWN_APPLICATION);
        }
        component.sendTemplate(person.getEmail());

    }

    @Override
    public void sendCustomEmail(CustomEmail request) {
        commonValidation.validateNotNull(request, "request");
        commonValidation.validateName(request.getSubject(), "subject");
        commonValidation.validateNotNull(request.getBody(), "body");
        request.setBody(commonValidation.toSafeHtml(request.getBody()));
        PersonEntity person = em.find(PersonEntity.class, request.getRecipientId());
        if (person == null) {
            throw new ApplicationException(ExceptionType.UNKNOWN_APPLICATION);
        }
        component.sendTemplate(person.getEmail());
        component.send(person.getEmail(), request.getSubject(), request.getBody());
    }

}
