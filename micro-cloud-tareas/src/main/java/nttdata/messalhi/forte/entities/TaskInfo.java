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
    private String description;
    private String state;
    private int version;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    public TaskInfo() {
    }

    public TaskInfo(String description, String state, int version) {
        this.description = description;
        this.state = state;
        this.version = version;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String toStringJSON() {
        try {
            StringBuilder jsonBuilder = new StringBuilder("{");
            jsonBuilder.append("\"id\": \"").append(this.id).append("\", ");
            jsonBuilder.append("\"version\": \"").append(this.version).append("\", ");
            jsonBuilder.append("\"description\": \"").append(this.description).append("\", ");
            jsonBuilder.append("\"state\": \"").append(this.state).append("\"}");
            return jsonBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "{}"; 
        }
    }

}
