package core.application.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(schema = "APPLICATION", name = "EDUCATION")
public class EducationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", insertable = false, updatable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "PERSON_ID", insertable = false, updatable = false)
    private PersonEntity person;

    @Column(name = "START_YEAR")
    private String startYear;

    @Column(name = "END_YEAR")
    private String endYear;

    @Column(name = "CITY")
    private String city;

    @Column(name = "COUNTRY")
    private String country;

    @Column(name = "SCHOOL_NAME")
    private String schoolName;

    @Column(name = "SCHOOL_TYPE")
    private String schoolType;

    @Column(name = "DEGREE")
    private Integer degree;
    
    public EducationEntity() {
    }

    public EducationEntity(PersonEntity person, String startYear, String endYear, String city, String country, String schoolName, String schoolType,
            Integer degree) {
        this.person = person;
        this.startYear = startYear;
        this.endYear = endYear;
        this.city = city;
        this.country = country;
        this.schoolName = schoolName;
        this.schoolType = schoolType;
        this.degree = degree;
    }

    public PersonEntity getPerson() {
        return person;
    }

    public void setPerson(PersonEntity person) {
        this.person = person;
    }

    public String getStartYear() {
        return startYear;
    }

    public void setStartYear(String startYear) {
        this.startYear = startYear;
    }

    public String getEndYear() {
        return endYear;
    }

    public void setEndYear(String endYear) {
        this.endYear = endYear;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getSchoolType() {
        return schoolType;
    }

    public void setSchoolType(String schoolType) {
        this.schoolType = schoolType;
    }

    public Integer getDegree() {
        return degree;
    }

    public void setDegree(Integer degree) {
        this.degree = degree;
    }

    public Long getId() {
        return id;
    }

}
