package nttdata.messalhi.forte.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

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
        ObjectMapper mapper = new ObjectMapper();
        try {
            ObjectNode jsonNode = mapper.createObjectNode();
            jsonNode.put("id", this.id);
            jsonNode.put("arn", this.arn);
            jsonNode.put("name", this.name);
            if (this.creationDate.toString() != null) {
                jsonNode.put("creationDate", this.creationDate.toString());
            }
            else{
                jsonNode.put("creationDate", "null");
            }
            jsonNode.put("userId", this.userId);

            ArrayNode taskDataArray = mapper.createArrayNode();
            for (int i = 0; i < this.taskInfo.size(); i++) {
                TaskInfo taskInfo = this.taskInfo.get(i);
                TaskSchedule taskSchedule = this.taskSchedule.get(i);
                TaskDestination taskDestination = this.taskDestination.get(i);

                ObjectNode taskNode = mapper.createObjectNode();
                taskNode.put("version", taskInfo.getVersion());
                taskNode.put("description", taskInfo.getDescription());
                taskNode.put("state", taskInfo.getState());
                if (taskSchedule.getStartDate() != null) {
                    taskNode.put("startDate", taskSchedule.getStartDate().toString());
                }
                else{
                    taskNode.put("startDate", "null");
                }
                if (taskSchedule.getEndDate() != null) {
                    taskNode.put("endDate", taskSchedule.getEndDate().toString());
                }
                else{
                    taskNode.put("endDate", "null");
                }

                taskNode.put("scheduleExpression", taskSchedule.getScheduleExpression());
                taskNode.put("timeZone", taskSchedule.getTimeZone());
                taskNode.put("maximumTimeWindowInMinutes", taskSchedule.getMaximumTimeWindowInMinutes());
                taskNode.put("url", taskDestination.getUrl());
                taskNode.put("httpMethod", taskDestination.getHttpMethod());
                taskNode.put("body", taskDestination.getBody());

                taskDataArray.add(taskNode);
            }
            jsonNode.set("taskData", taskDataArray);

            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);
        } catch (Exception e) {
            e.printStackTrace();
            return "{}";
        }
    }
    

}