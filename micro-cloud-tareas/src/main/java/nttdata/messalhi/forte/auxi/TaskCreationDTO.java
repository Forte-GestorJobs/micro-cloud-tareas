package nttdata.messalhi.forte.auxi;
import java.util.Date;



public class TaskCreationDTO {
    //TASKINFO
    private String name;
    private String description;
    private String state;
    private String userId;

    //TASKDESTINATION
    private String url;
    private String httpMethod;
    private String body;

    //TASKSCHEDULE
    private Date startDate;
    private Date endDate;
    private String scheduleExpression;
    private String timeZone;
    private int maximumTimeWindowInMinutes;

    public TaskCreationDTO(){
        //
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
}
