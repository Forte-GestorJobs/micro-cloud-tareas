package nttdata.messalhi.forte.entities;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "taskinfo")
public class TaskInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_generator")
    @SequenceGenerator(name="id_generator", sequenceName = "id_seq", initialValue = 100000, allocationSize = 1)
    private Long id;
    private String arn;
    private String name;
    private String description;
    private String state;
    private Date creationDate;
    private Date lastModification;
    private String userId;

    @OneToOne(optional = false, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private TaskDestination target;

    /*
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @MapsId
    private TaskSchedule schedule;
    */

    public TaskInfo() {
    }

    public TaskInfo(String name, String description, String state, String userId) {
        this.name = name;
        this.description = description;
        this.state = state;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getArn() {
        return arn;
    }

    public void setArn(String arn) {
        this.arn = arn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getLastModification() {
        return lastModification;
    }

    public void setLastModification(Date lastModification) {
        this.lastModification = lastModification;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public TaskDestination getTarget() {
        return target;
    }

    public void setTarget(TaskDestination target) {
        this.target = target;
    }

    public String toStringJSON() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "{}"; // Manejo de errores
        }
    }

}
