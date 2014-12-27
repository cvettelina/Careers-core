package core.application;

import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import core.application.builders.EntityBuilder;
import core.application.builders.ResponseBuilder;
import core.application.entities.InterviewEntity;
import core.application.entities.PersonEntity;
import core.application.entities.UserEntity;
import core.application.exception.ApplicationException;
import core.application.exception.ExceptionType;
import core.application.validations.CommonValidation;
import core.application.validations.InterviewValidation;

import api.application.Interviews;
import api.application.request.Interview;
import api.application.request.InterviewDuration;
import api.application.request.Status;
import api.application.response.InterviewResponse;

@Stateless
public class InterviewsService implements Interviews {

    @PersistenceContext
    EntityManager em;

    @EJB
    CommonValidation commonValidation;

    @EJB
    InterviewValidation interviewValidation;

    @SuppressWarnings("unchecked")
    @Override
    public Collection<InterviewResponse> getByUser(Long userId) {
        commonValidation.validateNotNull(userId, "userId");
        Query query = em.createNamedQuery(InterviewEntity.GET_BY_USER);
        query.setParameter("userId", userId);
        List<InterviewEntity> results = query.getResultList();
        ResponseBuilder builder = new ResponseBuilder();
        return builder.buildInterviewResponse(results);
    }

    @Override
    public InterviewResponse getbyApplicationId(Long applicationId) {
        commonValidation.validateNotNull(applicationId, "applicationId");
        Query query = em.createNamedQuery(InterviewEntity.GET_BY_APPLICATION);
        query.setParameter("applicationId", applicationId);
        query.setMaxResults(1);
        try {
            return (InterviewResponse) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public void add(Interview interview) {
        interviewValidation.validate(interview);
        PersonEntity person = em.find(PersonEntity.class, interview.getPersonId());
        if (person == null) {
            throw new ApplicationException(ExceptionType.UNKNOWN_APPLICATION);
        }
        UserEntity user = em.find(UserEntity.class, interview.getUserId());
        if (user == null) {
            throw new ApplicationException(ExceptionType.UNKNOWN_USER);
        }

        EntityBuilder builder = new EntityBuilder();
        InterviewEntity interviewEntity = builder.buildInterviewEntity(interview, person, user);
        em.persist(interviewEntity);
        person.setStatus(Status.INTERVIEW.name());
        em.merge(person);
    }

    @Override
    public void edit(Long interviewId, InterviewDuration duration) {
        commonValidation.validateNotNull(duration, "duration");
        commonValidation.validateNotNull(duration.getStartDate(), "startDate");
        InterviewEntity interview  = em.find(InterviewEntity.class, interviewId);
        if (interview == null) {
            throw new ApplicationException(ExceptionType.UNKNOWN_INTERVIEW);
        }
        boolean changes = false;
        if (!duration.getStartDate().equals(interview.getStartDate())) {
            interview.setStartDate(duration.getStartDate());
            changes = true;
        }
        if (duration.getEndDate() != null && !duration.getEndDate().equals(interview.getEndDate())) {
            interview.setEndDate(duration.getEndDate());
            changes = true;
        }
        if (changes) {
            em.merge(interview);
        }
    }

}
