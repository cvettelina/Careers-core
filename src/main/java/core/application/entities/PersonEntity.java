package core.application.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.NamedQuery;

@Entity
@Table(schema = "APPLICATION", name = "PERSON")
@NamedQueries({ @NamedQuery(name = PersonEntity.GET_ALL, query = "SELECT pe FROM PersonEntity pe"),
        @NamedQuery(name = PersonEntity.GET_BY_EMAIL, query = "SELECT pe FROM PersonEntity pe WHERE pe.email = :email") })
public class PersonEntity {

    public static final String GET_ALL = "getAll";
    public static final String GET_BY_EMAIL = "getByEmail";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", insertable = false, updatable = false)
    private Long id;

    @Column(name = "FIRSTNAME")
    private String firstName;

    @Column(name = "LASTNAME")
    private String lastName;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "GENDER")
    private String gender;

    @Column(name = "DATE_OF_BIRTH")
    private String dateOfBirth;

    @Column(name = "IP_ADDRESS")
    private String ipAddress;

    @Column(name = "INSERT_DATE")
    private Date insertDate;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "PICTURE")
    private String picture;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "ADDRESS_ID", nullable = false)
    private AddressEntity address;

    @OneToMany(targetEntity = EducationEntity.class, fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "PERSON_ID", referencedColumnName = "ID", nullable = false)
    private List<EducationEntity> education;

    @OneToMany(targetEntity = EmployementEntity.class, fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "PERSON_ID", referencedColumnName = "ID", nullable = false)
    private List<EmployementEntity> employement;

    @OneToMany(targetEntity = SkillEntity.class, fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "PERSON_ID", referencedColumnName = "ID", nullable = false)
    private List<SkillEntity> skills;

    @ManyToMany
    @JoinTable(name = "PERSON_POSITION", joinColumns = { @JoinColumn(name = "PERSON_ID") }, inverseJoinColumns = { @JoinColumn(name = "POSITION_ID") })
    private List<PositionEntity> positions;

    @Column(name = "TOTAL_EXPERIENCE")
    private Integer totalExperience;

    @Column(name = "QUALIFICATION")
    private Integer qualification;

    @Column(name = "COMMENT")
    private String comment;

    @Column(name = "UPDATE_DATE")
    private Date updateDate;

    @Column(name = "UPDATED_BY")
    private Long updatedBy;

    public PersonEntity() {
        this.insertDate = new Date();
        this.positions = new ArrayList<PositionEntity>();
    }

    public PersonEntity(String firstName, String lastName, String email, String dateOfBirth, String ipAddress, String status, String picture,
            AddressEntity address, List<EducationEntity> education, List<EmployementEntity> employement, List<SkillEntity> skills) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.ipAddress = ipAddress;
        this.insertDate = new Date();
        this.status = status;
        this.picture = picture;
        this.address = address;
        this.education = education;
        this.employement = employement;
        this.skills = skills;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public AddressEntity getAddress() {
        return address;
    }

    public void setAddress(AddressEntity address) {
        this.address = address;
    }

    public List<EducationEntity> getEducation() {
        return education;
    }

    public void setEducation(List<EducationEntity> education) {
        this.education = education;
    }

    public List<EmployementEntity> getEmployement() {
        return employement;
    }

    public void setEmployement(List<EmployementEntity> employement) {
        this.employement = employement;
    }

    public List<SkillEntity> getSkills() {
        return skills;
    }

    public void setSkills(List<SkillEntity> skills) {
        this.skills = skills;
    }

    public Long getId() {
        return id;
    }

    public Date getInsertDate() {
        return insertDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<PositionEntity> getPositions() {
        return positions;
    }

    public void setPositions(List<PositionEntity> positions) {
        this.positions = positions;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Integer getTotalExperience() {
        return totalExperience;
    }

    public void setTotalExperience(Integer totalExperience) {
        this.totalExperience = totalExperience;
    }

    public Integer getQualification() {
        return qualification;
    }

    public void setQualification(Integer qualification) {
        this.qualification = qualification;
    }

    public void addPosition(PositionEntity entity) {
        positions.add(entity);
    }

}
