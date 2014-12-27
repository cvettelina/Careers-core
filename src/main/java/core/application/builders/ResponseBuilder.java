package core.application.builders;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import api.application.request.Address;
import api.application.request.Degree;
import api.application.request.Duration;
import api.application.request.Education;
import api.application.request.Employment;
import api.application.request.Gender;
import api.application.request.PersonalInformation;
import api.application.request.SchoolType;
import api.application.request.Status;
import api.application.response.ApplicationResponse;
import api.application.response.InterviewResponse;
import api.application.response.PositionResponse;
import api.application.response.UserResponse;

import core.application.entities.AddressEntity;
import core.application.entities.EducationEntity;
import core.application.entities.EmployementEntity;
import core.application.entities.InterviewEntity;
import core.application.entities.PersonEntity;
import core.application.entities.PositionEntity;
import core.application.entities.SkillEntity;
import core.application.entities.UserEntity;

public class ResponseBuilder {

    public List<ApplicationResponse> buildApplicationResponse(List<PersonEntity> apps, Long position) {
        List<ApplicationResponse> response = new ArrayList<ApplicationResponse>();
        if (position != null) {
            for (PersonEntity entity : apps) {
                ApplicationResponse application = buildApplication(entity);
                for (PositionResponse positionResponce : application.getPositions()) {
                    if (positionResponce.getId().equals(position)) {
                        response.add(application);
                    }
                }
            }
        } else {
            for (PersonEntity entity : apps) {
                ApplicationResponse application = buildApplication(entity);
                response.add(application);
            }
        }
        return response;
    }

    public List<InterviewResponse> buildInterviewResponse(List<InterviewEntity> interviews) {
        List<InterviewResponse> responses = new ArrayList<InterviewResponse>();
        for (InterviewEntity entity : interviews) {
            InterviewResponse response = new InterviewResponse();
            response.setApplication(buildApplication(entity.getPerson()));
            response.setStartDate(entity.getStartDate());
            response.setEndDate(entity.getEndDate());
            response.setComment(entity.getComment());
            response.setId(entity.getId());
            responses.add(response);
        }
        return responses;
    }

    public UserResponse buildUserResponse(UserEntity userEntity) {
        UserResponse response = new UserResponse();
        response.setActive(userEntity.getActive() == 1 ? true : false);
        response.setEmail(userEntity.getEmail());
        response.setId(userEntity.getId());
        response.setType(userEntity.getType());
        response.setUsername(userEntity.getUsername());
        return response;
    }

    public ApplicationResponse buildApplication(PersonEntity personEntity) {
        ApplicationResponse application = new ApplicationResponse();
        List<Education> education = buildEducation(personEntity.getEducation());
        application.setEducation(education);
        application.setPersonalInformation(buildPersonalInformation(personEntity));
        application.setEmployement(buildEmployement(personEntity.getEmployement()));
        application.setComment(personEntity.getComment());
        application.setPositions(getPosiotions(personEntity.getPositions()));
        if (personEntity.getQualification() != null) {
            application.setQualification(Degree.fromInt(personEntity.getQualification()));
        }
        application.setStatus(Status.valueOf(personEntity.getStatus()));
        application.setTotalExperience(personEntity.getTotalExperience());
        application.setId(personEntity.getId());
        if (!personEntity.getSkills().isEmpty()) {
            for (SkillEntity skill : personEntity.getSkills()) {
                if ("PERSONAL".equals(skill.getType())) {
                    application.setPersonalSkills(skill.getDescription());
                } else {
                    application.setTechnicalSkills(skill.getDescription());
                }
            }
        } else {
            application.setTechnicalSkills("");
            application.setPersonalSkills("");
        }
        return application;
    }

    private Collection<PositionResponse> getPosiotions(List<PositionEntity> positions) {
        Collection<PositionResponse> positionsIds = new ArrayList<PositionResponse>();
        for (PositionEntity entity : positions) {
            PositionResponse position = new PositionResponse();
            position.setDescription(entity.getDescription());
            position.setId(entity.getId());
            position.setTitle(entity.getTitle());
            positionsIds.add(position);
        }
        return positionsIds;
    }

    private List<Employment> buildEmployement(List<EmployementEntity> employementEntities) {
        List<Employment> employements = new ArrayList<Employment>();
        for (EmployementEntity entity : employementEntities) {
            Employment employement = new Employment();
            employement.setCity(entity.getCity());
            employement.setCountry(entity.getCountry());
            employement.setCompanyName(entity.getCompanyName());
            employement.setDescription(entity.getDescription());
            employement.setDuration(new Duration(Integer.parseInt(entity.getStartYear()), Integer.parseInt(entity.getEndYear())));
            employement.setPosition(entity.getPosition());
            employements.add(employement);
        }
        return employements;
    }

    public List<Education> buildEducation(List<EducationEntity> educationEntities) {
        List<Education> educationList = new ArrayList<Education>();
        for (EducationEntity entity : educationEntities) {
            Education education = new Education();
            education.setCity(entity.getCity());
            education.setCountry(entity.getCountry());
            education.setDegree(Degree.fromInt(entity.getDegree()));
            education.setDuration(new Duration(Integer.parseInt(entity.getStartYear()), Integer.parseInt(entity.getEndYear())));
            education.setSchoolName(entity.getSchoolName());
            education.setSchoolType(SchoolType.valueOf(entity.getSchoolType()));
            educationList.add(education);
        }
        return educationList;
    }

    public PersonalInformation buildPersonalInformation(PersonEntity personEntity) {
        PersonalInformation personalInformation = new PersonalInformation();
        personalInformation.setDateOfBirth(personEntity.getDateOfBirth());
        personalInformation.setEmail(personEntity.getEmail());
        personalInformation.setFirstName(personEntity.getFirstName());
        personalInformation.setLastName(personEntity.getLastName());
        personalInformation.setAddress(buildAddress(personEntity.getAddress()));
        personalInformation.setGender(Gender.valueOf(personEntity.getGender()));
        return personalInformation;
    }

    public Address buildAddress(AddressEntity addressEntity) {
        Address address = new Address();
        address.setAddressLine(addressEntity.getAddressLine());
        address.setCity(addressEntity.getCity());
        address.setCountry(addressEntity.getCountry());
        address.setPhoneNumber(addressEntity.getPhoneNumber());
        return address;
    }

}
