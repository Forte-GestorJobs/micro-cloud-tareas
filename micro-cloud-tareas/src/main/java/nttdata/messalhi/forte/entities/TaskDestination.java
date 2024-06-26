package nttdata.messalhi.forte.entities;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "taskdestination")
public class TaskDestination {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_generator")
    @SequenceGenerator(name="id_generator", sequenceName = "id_seq", initialValue = 100000, allocationSize = 1)
    private Long id;
    private String url;
    private String httpMethod;
    private String body;
    private int version;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    public TaskDestination() {
    }

    public TaskDestination(String url, String httpMethod, String body, int version) {
        this.url = url;
        this.httpMethod = httpMethod;
        this.body = body;
        this.version = version;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
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
            jsonBuilder.append("\"url\": \"").append(this.url).append("\", ");
            jsonBuilder.append("\"httpMethod\": \"").append(this.httpMethod).append("\", ");
            jsonBuilder.append("\"body\": \"").append(this.body).append("\"}");
            return jsonBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "{}";
        }
    }

}
