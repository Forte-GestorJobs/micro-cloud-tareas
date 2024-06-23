package nttdata.messalhi.forte.entities;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "taskdestination")
public class TaskDestination {
    @Id
    private String id;
    private String url;
    private String httpMethod;
    private String body;
    private int version;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    public TaskDestination() {
    }

    public TaskDestination(String id, String url, String httpMethod, String body, int version) {
        this.id = id;
        this.url = url;
        this.httpMethod = httpMethod;
        this.body = body;
        this.version = version;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
