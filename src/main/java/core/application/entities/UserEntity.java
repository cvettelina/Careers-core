package core.application.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(schema = "APPLICATION", name = "USER")
@NamedQueries({ @NamedQuery(name = UserEntity.GET_BY_NAME, query = "SELECT ue FROM UserEntity ue WHERE ue.username = :username"),
        @NamedQuery(name = UserEntity.GET_BY_EMAIL, query = "SELECT ue FROM UserEntity ue WHERE ue.email = :email") })
public class UserEntity {

    public static final String GET_BY_NAME = "GetByName";
    public static final String GET_BY_EMAIL = "GetByEmail";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", insertable = false, updatable = false)
    private Long id;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "DATE_CREATED")
    private Date created;

    @Column(name = "LAST_UPDATED")
    private Date lastUpdated;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "ACTIVE")
    private Integer active;

    public UserEntity() {
        this.created = new Date();
    }

    public UserEntity(String username, String password, String email, String type, Integer active) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.created = new Date();
        this.type = type;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public Date getCreated() {
        return created;
    }

}
