package core.application.entities;

import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(schema = "APPLICATION", name = "CREDENTIALS")
@NamedQueries({ @NamedQuery(name = CredentialsEntity.GET_BY_USER, query = "SELECT ce FROM CredentialsEntity ce WHERE ce.user.id = :userId") })
public class CredentialsEntity {
    
    public static final String GET_BY_USER="GetCredentialsByUser";
    
    @Id
    @Column(name="TOKEN")
    private String token;
    

    @ManyToOne(targetEntity = UserEntity.class, fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID", nullable = false)
    private UserEntity user;
    
    @Column(name="LOGIN_TIME")
    private Timestamp loginTime;

    public CredentialsEntity(String token, UserEntity user, Timestamp loginTime) {
        this.token = token;
        this.user = user;
        this.loginTime = loginTime;
    }

    public CredentialsEntity() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public Timestamp getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Timestamp loginTime) {
        this.loginTime = loginTime;
    }
    
}
