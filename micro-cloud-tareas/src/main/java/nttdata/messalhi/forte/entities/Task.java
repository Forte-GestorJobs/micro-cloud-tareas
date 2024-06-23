package nttdata.messalhi.forte.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "task")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_generator")
    @SequenceGenerator(name="id_generator", sequenceName = "id_seq", initialValue = 100000, allocationSize = 1)
    private Long id;
    private String arn;
    private String name;
    private Date creationDate;
    private String userId;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskInfo> taskInfo;
    
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskSchedule> taskSchedule;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskDestination> taskDestination;


    public Task() {
    }
    public Task(String name, String userId) {
        this.name = name;
        this.userId = userId;
        this.creationDate = new Date();
        this.taskInfo = new ArrayList<>();
        this.taskSchedule = new ArrayList<>();
        this.taskDestination = new ArrayList<>();
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

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<TaskInfo> getTaskInfo() {
        return taskInfo;
    }

    public void setTaskInfo(List<TaskInfo> taskInfo) {
        this.taskInfo = taskInfo;
    }

    public List<TaskSchedule> getTaskSchedule() {
        return taskSchedule;
    }

    public void setTaskSchedule(List<TaskSchedule> taskSchedule) {
        this.taskSchedule = taskSchedule;
    }

    public List<TaskDestination> getTaskDestination() {
        return taskDestination;
    }

    public void setTaskDestination(List<TaskDestination> taskDestination) {
        this.taskDestination = taskDestination;
    }

    public void addTaskInfo(TaskInfo taskInfo) {
        this.taskInfo.add(taskInfo);
        taskInfo.setTask(this);
    }

    public void removeTaskInfo(TaskInfo taskInfo) {
        this.taskInfo.remove(taskInfo);
        taskInfo.setTask(null);
    }

    public void addTaskSchedule(TaskSchedule taskSchedule) {
        this.taskSchedule.add(taskSchedule);
        taskSchedule.setTask(this);
    }

    public void removeTaskSchedule(TaskSchedule taskSchedule) {
        this.taskSchedule.remove(taskSchedule);
        taskSchedule.setTask(null);
    }

    public void addTaskDestination(TaskDestination taskDestination) {
        this.taskDestination.add(taskDestination);
        taskDestination.setTask(this);
    }

    public void removeTaskDestination(TaskDestination taskDestination) {
        this.taskDestination.remove(taskDestination);
        taskDestination.setTask(null);
    }

    public String toStringJSON() {
        try {
            StringBuilder jsonBuilder = new StringBuilder("{");
            jsonBuilder.append("\"id\": \"").append(this.id).append("\", ");
            jsonBuilder.append("\"arn\": \"").append(this.arn).append("\", ");
            jsonBuilder.append("\"name\": \"").append(this.name).append("\", ");
            jsonBuilder.append("\"creationDate\": \"").append(this.creationDate).append("\", ");
            jsonBuilder.append("\"userId\": \"").append(this.userId).append("\", ");
            
            // Agregar tamaño de las listas
            jsonBuilder.append("\"taskInfoSize\": \"").append(this.taskInfo.size()).append("\", ");
            jsonBuilder.append("\"taskScheduleSize\": \"").append(this.taskSchedule.size()).append("\", ");
            jsonBuilder.append("\"taskDestinationSize\": \"").append(this.taskDestination.size()).append("\", ");
            
            // Serializar los elementos de taskInfo
            jsonBuilder.append("\"taskInfo\": [");
            for (int i = 0; i < this.taskInfo.size(); i++) {
                TaskInfo taskInfo = this.taskInfo.get(i);
                jsonBuilder.append(taskInfo.toStringJSON());
                if (i < this.taskInfo.size() - 1) {
                    jsonBuilder.append(", ");
                }
            }
            jsonBuilder.append("], ");
            
            // Serializar los elementos de taskSchedule
            jsonBuilder.append("\"taskSchedule\": [");
            for (int i = 0; i < this.taskSchedule.size(); i++) {
                TaskSchedule taskSchedule = this.taskSchedule.get(i);
                jsonBuilder.append(taskSchedule.toStringJSON());
                if (i < this.taskSchedule.size() - 1) {
                    jsonBuilder.append(", ");
                }
            }
            jsonBuilder.append("], ");
            
            // Serializar los elementos de taskDestination
            jsonBuilder.append("\"taskDestination\": [");
            for (int i = 0; i < this.taskDestination.size(); i++) {
                TaskDestination taskDestination = this.taskDestination.get(i);
                jsonBuilder.append(taskDestination.toStringJSON());
                if (i < this.taskDestination.size() - 1) {
                    jsonBuilder.append(", ");
                }
            }
            jsonBuilder.append("]");
            
            jsonBuilder.append("}");
            return jsonBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "{}"; // Manejo de errores
        }
    }
    

}