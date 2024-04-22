package nttdata.messalhi.forte.entities;

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

    @OneToOne(optional = false, cascade = CascadeType.ALL, orphanRemoval = true)
    private TaskDestination target;

    @OneToOne(mappedBy = "taskInfo", optional = false, cascade = CascadeType.ALL, orphanRemoval = true)
    private TaskSchedule schedule;

    public TaskInfo() {
    }

    public TaskInfo(String name, String description, String state, String userId) {
        this.name = name;
        this.description = description;
        this.state = state;
        this.userId = userId;
        this.creationDate = new Date();
        this.lastModification = new Date();
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

    public TaskSchedule getSchedule() {
        return schedule;
    }

    public void setSchedule(TaskSchedule schedule) {
        this.schedule = schedule;
    }

    public String toStringJSON() {
        try {
            StringBuilder jsonBuilder = new StringBuilder("{");
            jsonBuilder.append("\"id\": \"").append(this.id).append("\", ");
            jsonBuilder.append("\"arn\": \"").append(this.arn).append("\", ");
            jsonBuilder.append("\"name\": \"").append(this.name).append("\", ");
            jsonBuilder.append("\"description\": \"").append(this.description).append("\", ");
            jsonBuilder.append("\"state\": \"").append(this.state).append("\", ");
            jsonBuilder.append("\"creationDate\": \"").append(this.creationDate).append("\", ");
            jsonBuilder.append("\"lastModification\": \"").append(this.lastModification).append("\", ");
            jsonBuilder.append("\"userId\": \"").append(this.userId).append("\", ");
            jsonBuilder.append("\"target\": ").append(this.target.toStringJSON()).append(", ");
            jsonBuilder.append("\"schedule\": ").append(this.schedule.toStringJSON()).append("}");
            return jsonBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "{}"; // Manejo de errores
        }
    }

}
