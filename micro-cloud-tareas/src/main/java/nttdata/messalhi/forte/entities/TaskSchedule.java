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
    private String scheduleExpression;
    private String timeZone;
    private int version;
    private int maximumTimeWindowInMinutes;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    public TaskSchedule() {
    }

    public TaskSchedule(String id, Date startDate, Date endDate, String scheduleExpression, String timeZone, int maximumTimeWindowInMinutes, int version) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.scheduleExpression = scheduleExpression;
        this.timeZone = timeZone;
        this.maximumTimeWindowInMinutes = maximumTimeWindowInMinutes;
        this.version = version;
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

    public int getMaximumTimeWindowInMinutes() {
        return maximumTimeWindowInMinutes;
    }

    public void setMaximumTimeWindowInMinutes(int maximumTimeWindowInMinutes) {
        this.maximumTimeWindowInMinutes = maximumTimeWindowInMinutes;
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
            jsonBuilder.append("\"startDate\": \"").append(this.startDate).append("\", ");
            jsonBuilder.append("\"endDate\": \"").append(this.endDate).append("\", ");
            jsonBuilder.append("\"scheduleExpression\": \"").append(this.scheduleExpression).append("\", ");
            jsonBuilder.append("\"timeZone\": \"").append(this.timeZone).append("\", ");
            jsonBuilder.append("\"maximumTimeWindowInMinutes\": \"").append(this.maximumTimeWindowInMinutes).append("\"} ");
            return jsonBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "{}";
        }
    }
    
}
