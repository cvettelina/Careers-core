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
@Table(schema = "APPLICATION", name = "EMPLOYEMENT")
public class EmployementEntity {

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

    @Column(name = "COMPANY_NAME")
    private String companyName;

    @Column(name = "POSITION")
    private String position;

    @Column(name = "DESCRIPTION")
    private String description;

    public EmployementEntity() {
    }

    public EmployementEntity(PersonEntity person, String startYear, String endYear, String city, String country, String companyName, String position,
            String description) {
        this.person = person;
        this.startYear = startYear;
        this.endYear = endYear;
        this.city = city;
        this.country = country;
        this.companyName = companyName;
        this.position = position;
        this.description = description;
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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public PersonEntity getPerson() {
        return person;
    }

    public void setPerson(PersonEntity person) {
        this.person = person;
    }

}
