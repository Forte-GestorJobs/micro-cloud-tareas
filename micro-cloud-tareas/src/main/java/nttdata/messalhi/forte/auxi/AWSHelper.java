package nttdata.messalhi.forte.auxi;

import com.amazonaws.services.scheduler.AmazonScheduler;
import com.amazonaws.services.scheduler.AmazonSchedulerClientBuilder;
import com.amazonaws.services.scheduler.model.CreateScheduleRequest;
import com.amazonaws.services.scheduler.model.CreateScheduleResult;
import com.amazonaws.services.scheduler.model.FlexibleTimeWindow;
import com.amazonaws.services.scheduler.model.Target;
import nttdata.messalhi.forte.utils.ResultadoConsultaAWS;

public class AWSHelper {
    public static ResultadoConsultaAWS crearSchedule(String name, String description, String state, String scheduleExpression) {
        AmazonScheduler amazonScheduler = AmazonSchedulerClientBuilder.defaultClient();
        FlexibleTimeWindow flexibleTimeWindow = new FlexibleTimeWindow().withMode("OFF");
        Target target = new Target()
                        .withArn("arn:aws:lambda:eu-west-2:256762469783:function:a")
                        .withRoleArn("arn:aws:iam::256762469783:role/demo_rol_eb_y_lambda");
        try {
            CreateScheduleRequest request = new CreateScheduleRequest()
                    .withName(name)
                    .withDescription(description)
                    .withScheduleExpression(scheduleExpression)
                    .withState(state)
                    .withFlexibleTimeWindow(flexibleTimeWindow)
                    .withTarget(target);

            CreateScheduleResult result = amazonScheduler.createSchedule(request);
            amazonScheduler.shutdown();
            return new ResultadoConsultaAWS(true, result.getScheduleArn());
        } catch (Exception e) {
            return new ResultadoConsultaAWS(false,"Error al crear la regla: " + e.getMessage() + " " + state);
        }
    }

}

