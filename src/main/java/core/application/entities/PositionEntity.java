package core.application.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(schema = "APPLICATION", name = "POSITION")
@NamedQueries({ @NamedQuery(name = PositionEntity.GET_ALL, query = "SELECT pe FROM PositionEntity pe")})
public class PositionEntity {
    
    public static final String GET_ALL = "GetAllPositions";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", insertable = false, updatable = false)
    private Long id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "DESCRIPTION")
    private String description;
    
    @ManyToMany
    @JoinTable(name="PERSON_POSITION", joinColumns={@JoinColumn(name="POSITION_ID")}, inverseJoinColumns={@JoinColumn(name="PERSON_ID")})
    private List<PersonEntity> candidates;
    
    public PositionEntity() {
    }

    public PositionEntity(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<PersonEntity> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<PersonEntity> candidates) {
        this.candidates = candidates;
    }

}
