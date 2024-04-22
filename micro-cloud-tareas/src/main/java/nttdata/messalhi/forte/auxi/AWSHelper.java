package nttdata.messalhi.forte.auxi;

import com.amazonaws.services.scheduler.AmazonScheduler;
import com.amazonaws.services.scheduler.AmazonSchedulerClientBuilder;
import com.amazonaws.services.scheduler.model.*;
import nttdata.messalhi.forte.entities.TaskInfo;
import nttdata.messalhi.forte.utils.ResultadoConsultaAWS;

public class AWSHelper {
    private AWSHelper(){}
    public static ResultadoConsultaAWS createSchedule(TaskInfo taskInfo, String input){
        try {
            AmazonScheduler amazonScheduler = AmazonSchedulerClientBuilder.defaultClient();
            FlexibleTimeWindow flexibleTimeWindow;
            if (taskInfo.getSchedule().getMaximumTimeWindowInMinutes() == 0){
                flexibleTimeWindow = new FlexibleTimeWindow().withMode("OFF");
            }
            else{
                flexibleTimeWindow = new FlexibleTimeWindow().withMode("FLEXIBLE").withMaximumWindowInMinutes(taskInfo.getSchedule().getMaximumTimeWindowInMinutes());
            }
            Target target = new Target()
                    .withArn("arn:aws:lambda:eu-west-2:256762469783:function:write-event-db")
                    .withRoleArn("arn:aws:iam::256762469783:role/demo_rol_eb_y_lambda")
                    .withInput(input);
            CreateScheduleRequest request = new CreateScheduleRequest()
                    .withName(taskInfo.getUserId() + "." + taskInfo.getName())
                    .withDescription(taskInfo.getDescription())
                    .withScheduleExpression(taskInfo.getSchedule().getScheduleExpression())
                    .withState(taskInfo.getState())
                    .withFlexibleTimeWindow(flexibleTimeWindow)
                    .withTarget(target)
                    .withScheduleExpressionTimezone(taskInfo.getSchedule().getTimeZone())
                    .withStartDate(taskInfo.getSchedule().getStartDate())
                    .withEndDate(taskInfo.getSchedule().getEndDate())
                    ;

            CreateScheduleResult result = amazonScheduler.createSchedule(request);
            amazonScheduler.shutdown();
            return new ResultadoConsultaAWS(true, result.getScheduleArn());
        } catch (Exception e) {
            return new ResultadoConsultaAWS(false,"Error al crear la regla: " + e.getMessage());
        }
    }
    public static ResultadoConsultaAWS deleteSchedule(String name) {
        try {
            AmazonScheduler amazonScheduler = AmazonSchedulerClientBuilder.defaultClient();
            DeleteScheduleRequest deleteScheduleRequest = new DeleteScheduleRequest().withName(name);
            DeleteScheduleResult result = amazonScheduler.deleteSchedule(deleteScheduleRequest);
            amazonScheduler.shutdown();
            return new ResultadoConsultaAWS(true, result.toString());
        } catch (Exception e) {
            return new ResultadoConsultaAWS(false,"Error al eliminar la regla: " + e.getMessage());
        }
    }
    public static ResultadoConsultaAWS updateSchedule(String name, String description, String state, String scheduleExpression, Integer maximumWindowInMinutes){
        try{
            AmazonScheduler amazonScheduler = AmazonSchedulerClientBuilder.defaultClient();
            FlexibleTimeWindow flexibleTimeWindow;
            if (maximumWindowInMinutes == 0){
                flexibleTimeWindow = new FlexibleTimeWindow().withMode("OFF");
            }
            else{
                flexibleTimeWindow = new FlexibleTimeWindow().withMode("FLEXIBLE").withMaximumWindowInMinutes(maximumWindowInMinutes);
            }
            Target target = new Target()
                    .withArn("arn:aws:lambda:eu-west-2:256762469783:function:write-event-db")
                    .withRoleArn("arn:aws:iam::256762469783:role/demo_rol_eb_y_lambda");
            UpdateScheduleRequest updateScheduleRequest = new UpdateScheduleRequest()
                    .withName(name)
                    .withDescription(description)
                    .withState(state)
                    .withScheduleExpression(scheduleExpression)
                    .withFlexibleTimeWindow(flexibleTimeWindow)
                    .withTarget(target);
            UpdateScheduleResult result = amazonScheduler.updateSchedule(updateScheduleRequest);
            amazonScheduler.shutdown();
            return new ResultadoConsultaAWS(true, result.toString()+" |" + scheduleExpression+"|");
        } catch (Exception e) {
            return new ResultadoConsultaAWS(false,"Error al actualizar la regla: " + e.getMessage());
        }
    }

}

