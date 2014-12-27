package core.application.validations;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import core.application.entities.PersonEntity;
import core.application.entities.PositionEntity;
import core.application.exception.ApplicationException;
import core.application.exception.ExceptionType;

import api.application.request.Address;
import api.application.request.Application;
import api.application.request.Duration;
import api.application.request.Education;
import api.application.request.Employment;
import api.application.request.PersonalInformation;
import api.application.request.Status;

@Stateless
public class ApplicationValidationComponent {

    public static final Integer MIN_AGE = 18;
    public static final Integer MAX_SKILL_LENGTH = 400;
    public static final String PHONE_REGEX = "^\\d{5,9}$";
    @PersistenceContext
    EntityManager em;

    @EJB
    CommonValidation common;

    public void validate(Application request) {
        validatePersonalInformation(request.getPersonalInformation());
        checkIfExists(request.getPersonalInformation().getEmail());
        if (request.getEducation() != null) {
            for (Education education : request.getEducation()) {
                validateEducation(education);
            }
        }
        if (request.getEmployment() != null) {
            for (Employment employment : request.getEmployment()) {
                validateEmployment(employment);
            }
        }
        request.setPersonalSkills(validateSkills(request.getPersonalSkills(), "social skills"));
        request.setTechnicalSkills(validateSkills(request.getTechnicalSkills(), "technical skills"));
        request.setComment(validateSkills(request.getComment(), "comment"));
    }

    private void validatePersonalInformation(PersonalInformation personalInformation) {
        common.validateNotNull(personalInformation, "personal information");
        common.validateName(personalInformation.getFirstName(), "first name");
        common.validateName(personalInformation.getLastName(), "last name");
        common.validateEmail(personalInformation.getEmail());
        validateDOB(personalInformation.getDateOfBirth());
        validateAddress(personalInformation.getAddress());

    }

    private void validateAddress(Address address) {
        common.validateNotNull(address, "address");
        address.setAddressLine(common.toSafeHtml(address.getAddressLine()));
        common.validateNotNull(address.getAddressLine(), "adressLine");
        common.validateName(address.getCity(), "city");
        common.validateName(address.getCountry(), "country");
        validatePhone(address.getPhoneNumber());
    }

    private void validateDOB(String dateOfBirth) {
        common.validateNotNull(dateOfBirth, "date of birth");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            date = sdf.parse(dateOfBirth);
        } catch (ParseException e) {
            throw new ApplicationException(ExceptionType.INVALID_PARAMETER, "date of birth");
        }
        Calendar calendar = Calendar.getInstance();
        int thisYear = calendar.get(Calendar.YEAR);
        calendar.setTime(date);
        int birthYear = calendar.get(Calendar.YEAR);

        if (thisYear - birthYear < MIN_AGE) {
            throw new ApplicationException(ExceptionType.INVALID_PARAMETER, "date of birth");
        }

    }

    public void checkIfExists(String email) {
        Query query = em.createNamedQuery(PersonEntity.GET_BY_EMAIL);
        query.setParameter("email", email);
        PersonEntity entity = null;
        try {
            entity = (PersonEntity) query.getSingleResult();
            if (entity != null) {
                throw new ApplicationException(ExceptionType.APPLICATION_ALREADY_SENT);
            }
        } catch (NoResultException e) {
        }
    }

    public String validateSkills(String skill, String paramName) {
        if (skill != null) {
            if (skill.length() >= MAX_SKILL_LENGTH) {
                throw new ApplicationException(ExceptionType.INVALID_PARAMETER, paramName);
            }
        }
        return common.toSafeHtml(skill);
    }

    public void validateEmployment(Employment employment) {
        if (employment == null) {
            return;
        }
        common.validateName(employment.getCity(), "city");
        common.validateName(employment.getCountry(), "country");
        common.validateNameWithDigits(employment.getPosition(), "position");
        common.validateNameWithDigits(employment.getCompanyName(), "company name");
        validateDuration(employment.getDuration());
        if (employment.getDescription() != null && !"".equals(employment.getDescription())) {
            employment.setDescription(common.toSafeHtml(employment.getDescription()));
        }
    }

    public void validateEducation(Education education) {
        if (education == null) {
            return;
        }
        common.validateName(education.getCity(), "city");
        common.validateName(education.getCountry(), "country");
        common.validateNameWithDigits(education.getSchoolName(), "school name");
        validateDuration(education.getDuration());
    }

    public void validateDuration(Duration duration) {
        common.validateNotNull(duration, "duartion");
        common.validateNotNull(duration.getEndYear(), "end Year");
        common.validateNotNull(duration.getStartYear(), "start Year");
        if (duration.getStartYear() < 1900) {
            throw new ApplicationException(ExceptionType.INVALID_PARAMETER, "start year");
        }
        if (duration.getEndYear() < duration.getStartYear()) {
            throw new ApplicationException(ExceptionType.INVALID_PARAMETER, "end year");
        }
    }

    public void validateApplicatinId(Long applicationId) {
        common.validateNotNull(applicationId, "applicationId");
        PersonEntity personEntity = em.find(PersonEntity.class, applicationId);
        if (personEntity == null) {
            throw new ApplicationException(ExceptionType.UNKNOWN_APPLICATION);
        }
    }

    private void validatePhone(String phoneNumber) {
        common.validateNotNull(phoneNumber, "phone number");
        if (!phoneNumber.matches(PHONE_REGEX)) {
            throw new ApplicationException(ExceptionType.INVALID_PARAMETER, "phone number");
        }
    }
}
