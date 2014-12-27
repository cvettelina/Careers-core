package core.application.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import core.application.EmailType;

@Entity
@Table(schema = "APPLICATION", name = "EMAIL_TEMPLATE")
public class EmailTemplateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", insertable = false, updatable = false)
    private Long id;

    @Column(name = "SUBJECT")
    private String subject;

    @Column(name = "BODY")
    private String body;
    
    @Column(name = "TYPE")
    private EmailType type;

    public EmailTemplateEntity() {
    }

    public EmailTemplateEntity(String subject, String body, EmailType type) {
        this.subject = subject;
        this.body = body;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public EmailType getType() {
        return type;
    }

    public void setType(EmailType type) {
        this.type = type;
    }
}
