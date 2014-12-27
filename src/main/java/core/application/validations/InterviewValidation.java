package core.application.validations;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import api.application.request.Interview;

@Stateless
public class InterviewValidation {

    @EJB
    CommonValidation commonValidation;

    public void validate(Interview interview) {
        commonValidation.validateNotNull(interview, "interview");
        commonValidation.validateNotNull(interview.getPersonId(), "personId");
        commonValidation.validateNotNull(interview.getUserId(), "userId");
        commonValidation.validateNotNull(interview.getStartDate(), "startHour");
        interview.setComment(commonValidation.toSafeHtml(interview.getComment()));
    }

}
