package core.application.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(schema = "APPLICATION", name = "CONFIG")
@NamedQueries({ @NamedQuery(name = ConfigEntity.GET_ALL, query = "SELECT pe FROM ConfigEntity pe") })
public class ConfigEntity {

    public static final String GET_ALL = "GetAll";

    @Id
    @Column(name = "NAME")
    private String name;

    @Column(name = "VALUE")
    private String value;

    public ConfigEntity() {
    }

    public ConfigEntity(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

}
