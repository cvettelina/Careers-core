package core.application.builders;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import api.application.request.Address;
import api.application.request.Application;
import api.application.request.Degree;
import api.application.request.Education;
import api.application.request.Employment;
import api.application.request.Interview;
import api.application.request.PersonalInformation;
import api.application.request.Status;
import api.application.request.User;
import core.application.entities.AddressEntity;
import core.application.entities.EducationEntity;
import core.application.entities.EmployementEntity;
import core.application.entities.InterviewEntity;
import core.application.entities.PersonEntity;
import core.application.entities.PositionEntity;
import core.application.entities.SkillEntity;
import core.application.entities.UserEntity;

public class EntityBuilder {

    public PersonEntity buildPerson(Application request, PositionEntity entity) {
        PersonEntity person = new PersonEntity();
        PersonalInformation personalInfo = request.getPersonalInformation();
        person.setFirstName(personalInfo.getFirstName());
        person.setLastName(personalInfo.getLastName());
        person.setEmail(personalInfo.getEmail());
        person.setDateOfBirth(personalInfo.getDateOfBirth());
        person.setStatus(Status.NOT_CHECKED.name());
        person.setGender(personalInfo.getGender().name());
        AddressEntity address = buildAddress(personalInfo.getAddress());
        person.setAddress(address);
        if (request.getEducation() != null) {
            buildEducation(request.getEducation(), person);
        } else {
            person.setQualification(Degree.No_Education.getValue());
        }
        if (request.getEmployment() != null) {
            List<EmployementEntity> employement = buildEmployement(request.getEmployment(), person);
            person.setEmployement(employement);
        } else {
            person.setTotalExperience(0);
        }
        List<SkillEntity> skills = new ArrayList<SkillEntity>();
        if (request.getPersonalSkills() != null) {
            skills.add(getSkills("PERSONAL", request.getPersonalSkills()));
        }
        if (request.getTechnicalSkills() != null) {
            skills.add(getSkills("TECHNICAL", request.getTechnicalSkills()));
        }
        person.setSkills(skills);
        person.addPosition(entity);
        return person;
    }

    private SkillEntity getSkills(String type, String skills) {
        SkillEntity entity = new SkillEntity();
        entity.setType(type);
        entity.setDescription(skills);
        return entity;
    }

    public List<EmployementEntity> buildEmployement(List<Employment> employement, PersonEntity person) {
        List<EmployementEntity> employements = new ArrayList<EmployementEntity>();
        Integer experience = 0;
        for (Employment item : employement) {
            EmployementEntity entity = new EmployementEntity();
            entity.setCity(item.getCity());
            entity.setCountry(item.getCountry());
            entity.setPosition(item.getPosition());
            entity.setCompanyName(item.getCompanyName());
            entity.setDescription(item.getDescription());
            entity.setStartYear(item.getDuration().getStartYear().toString());
            entity.setEndYear(item.getDuration().getEndYear().toString());
            employements.add(entity);
            int duration = 0;
            if (item.getDuration().getStartYear() != null && item.getDuration().getEndYear() != null) {
                if (0 == item.getDuration().getEndYear()) {
                    Calendar calendar = Calendar.getInstance();
                    duration = item.getDuration().getEndYear() - calendar.get(Calendar.YEAR);
                } else {
                    duration = item.getDuration().getEndYear() - item.getDuration().getStartYear();
                }
            }
            experience += duration;
        }
        person.setTotalExperience(experience);
        return employements;
    }

    public void buildEducation(List<Education> education, PersonEntity person) {
        List<EducationEntity> educations = new ArrayList<EducationEntity>();
        Degree degree = Degree.No_Education;
        for (Education item : education) {
            EducationEntity entity = new EducationEntity();
            entity.setCity(item.getCity());
            entity.setCountry(item.getCountry());
            entity.setDegree(item.getDegree().getValue());
            entity.setSchoolName(item.getSchoolName());
            entity.setSchoolType(item.getSchoolType().name());
            entity.setStartYear(item.getDuration().getStartYear().toString());
            entity.setEndYear(item.getDuration().getEndYear().toString());
            educations.add(entity);
            if (degree.getValue() < item.getDegree().getValue()) {
                degree = item.getDegree();
            }
        }
        person.setQualification(degree.getValue());
        person.setEducation(educations);
    }

    public AddressEntity buildAddress(Address address) {
        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setCountry(address.getCountry());
        addressEntity.setCity(address.getCity());
        addressEntity.setAddressLine(address.getAddressLine());
        addressEntity.setPhoneNumber(address.getPhoneNumber());
        return addressEntity;
    }

    public UserEntity buildUserEntity(User user) {
        UserEntity userEntity = new UserEntity();
        userEntity.setActive(0);
        userEntity.setEmail(user.getEmail());
        userEntity.setType(user.getType());
        userEntity.setUsername(user.getUsername());
        return userEntity;
    }

    public InterviewEntity buildInterviewEntity(Interview interview, PersonEntity person, UserEntity user) {
        InterviewEntity entity = new InterviewEntity();
        entity.setPerson(person);
        entity.setUser(user);
        entity.setComment(interview.getComment());
        entity.setEndDate(interview.getEndDate());
        entity.setStartDate(interview.getStartDate());
        return entity;
    }

}
