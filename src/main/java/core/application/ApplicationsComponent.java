package core.application;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import api.application.request.Application;
import api.application.request.Degree;
import api.application.request.Status;
import api.application.response.ApplicationResponse;

import core.application.builders.EntityBuilder;
import core.application.builders.ResponseBuilder;
import core.application.entities.PersonEntity;
import core.application.entities.PositionEntity;
import core.application.entities.UserEntity;
import core.application.exception.ApplicationException;
import core.application.exception.ExceptionType;

@Stateless
public class ApplicationsComponent {

    @PersistenceContext
    EntityManager em;

    @EJB
    Config config;

    @EJB
    EmailsComponent emailComponent;

    public void saveApplication(Application request, String ipAddress) {
        EntityBuilder builder = new EntityBuilder();
        PositionEntity position = em.find(PositionEntity.class, request.getPositionId());
        if (position == null) {
            throw new ApplicationException(ExceptionType.UNKNOWN_POSITION);
        }
        PersonEntity person = builder.buildPerson(request, position);
        person.setIpAddress(ipAddress);
        em.persist(person);
    }

    @SuppressWarnings({ "unchecked" })
    public List<ApplicationResponse> getApplications(String filterQuery, Long position, Integer period, Status status, Degree degree) {
        Query query = em.createQuery(filterQuery, PersonEntity.class);
        if (status != null) {
            query.setParameter("status", status.name());
        }
        if (degree != null) {
            query.setParameter("degree", degree.getValue());
        }
        if (period != null) {
            query.setParameter("period", period);
        }
        List<PersonEntity> allApps = query.getResultList();
        ResponseBuilder builder = new ResponseBuilder();
        return builder.buildApplicationResponse(allApps, position);
    }

    public ApplicationResponse getById(Long applicationId) {
        PersonEntity personEntity = em.find(PersonEntity.class, applicationId);
        if (personEntity == null) {
            throw new ApplicationException(ExceptionType.UNKNOWN_APPLICATION);
        }
        ResponseBuilder builder = new ResponseBuilder();
        return builder.buildApplication(personEntity);
    }

    public void changeStatus(Long applicationId, Status status, Long userId) {
        PersonEntity person = em.find(PersonEntity.class, applicationId);
        if (person == null) {
            throw new ApplicationException(ExceptionType.UNKNOWN_APPLICATION);
        }
        UserEntity user = em.find(UserEntity.class, userId);
        if (user == null) {
            throw new ApplicationException(ExceptionType.UNKNOWN_USER);
        }

        if (!Status.NOT_CHECKED.equals(Status.valueOf(person.getStatus()))) {
            throw new ApplicationException(ExceptionType.UPDATE_NOT_ALLOWED);
        }

        if (Status.REJECTED.equals(Status.valueOf(person.getStatus()))) {
            String sendMail = config.getValue("email_auto_send");
            if ("1".equals(sendMail)) {
                emailComponent.sendTemplate(person.getEmail());
            }
        }
        person.setStatus(status.name());
        person.setUpdateDate(new Date());
        person.setUpdatedBy(userId);
    }
}
