package nttdata.messalhi.forte.entities;


import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "taskschedule")
public class TaskSchedule {
    @Id
    private String id;
    private Date startDate;
    private Date endDate;
    private Date creationDate;
    private String scheduleExpression;
    private String timeZone;

    private int maximumTimeWindowInMinutes;
    @ManyToOne
    @JoinColumn(name = "taskinfo_id")
    private TaskInfo taskInfo;

    public TaskSchedule() {
    }

    public TaskSchedule(String id, Date startDate, Date endDate, String scheduleExpression, String timeZone, int maximumTimeWindowInMinutes) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.creationDate = new Date();
        this.scheduleExpression = scheduleExpression;
        this.timeZone = timeZone;
        this.maximumTimeWindowInMinutes = maximumTimeWindowInMinutes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getScheduleExpression() {
        return scheduleExpression;
    }

    public void setScheduleExpression(String scheduleExpression) {
        this.scheduleExpression = scheduleExpression;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public TaskInfo getTaskInfo() {
        return taskInfo;
    }

    public void setTaskInfo(TaskInfo taskInfo) {
        this.taskInfo = taskInfo;
    }

    public int getMaximumTimeWindowInMinutes() {
        return maximumTimeWindowInMinutes;
    }

    public void setMaximumTimeWindowInMinutes(int maximumTimeWindowInMinutes) {
        this.maximumTimeWindowInMinutes = maximumTimeWindowInMinutes;
    }

    public String toStringJSON() {
        try {
            // Crear un objeto JSON con todos los campos de TaskSchedule y solo el ID de TaskInfo
            return "{ \"id\": \"" + this.id + "\", " +
                    "\"startDate\": \"" + this.startDate + "\", " +
                    "\"endDate\": \"" + this.endDate + "\", " +
                    "\"creationDate\": \"" + this.creationDate + "\", " +
                    "\"scheduleExpression\": \"" + this.scheduleExpression + "\", " +
                    "\"timeZone\": \"" + this.timeZone + "\", " +
                    "\"maximumTimeWindowInMinutes\": \"" + this.maximumTimeWindowInMinutes + "\", " +
                    "\"taskId\": \"" + this.taskInfo.getId() + "\" }";
        } catch (Exception e) {
            e.printStackTrace();
            return "{}"; // Manejo de errores
        }
    }
}
