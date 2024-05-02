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

    private int maximumTimeWindowInMinutes;

    public TaskSchedule() {
    }

    public TaskSchedule(String id, Date startDate, Date endDate, String scheduleExpression, String timeZone, int maximumTimeWindowInMinutes) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
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

    public String toStringJSON() {
        try {
            return "{ \"id\": \"" + this.id + "\", " +
                    "\"startDate\": \"" + this.startDate + "\", " +
                    "\"endDate\": \"" + this.endDate + "\", " +
                    "\"scheduleExpression\": \"" + this.scheduleExpression + "\", " +
                    "\"timeZone\": \"" + this.timeZone + "\", " +
                    "\"maximumTimeWindowInMinutes\": \"" + this.maximumTimeWindowInMinutes + "\" }";
        } catch (Exception e) {
            e.printStackTrace();
            return "{}";
        }
    }
}
