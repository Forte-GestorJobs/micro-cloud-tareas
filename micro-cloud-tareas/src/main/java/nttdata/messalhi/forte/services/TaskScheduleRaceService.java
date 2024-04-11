package nttdata.messalhi.forte.services;


import nttdata.messalhi.forte.auxi.AWSHelper;
import nttdata.messalhi.forte.entities.TaskSchedule;
import nttdata.messalhi.forte.utils.DatabaseResult;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TaskScheduleRaceService {

    public DatabaseResult addTaskSchedule(TaskSchedule taskSchedule) {
        //String arn = "1234";
        //String estado = taskSchedule.getState();
        String resultado = "ok";
        return new DatabaseResult(true, resultado);
    }

    public ResponseEntity<String> getTaskSchedule(String arn) {
        return null;
    }

    public ResponseEntity<String> listTaskSchedule(String user_id) {
        return null;
    }

    public ResponseEntity<String> deleteTaskSchedule(String arn) {
        return null;
    }

    public ResponseEntity<String> updateTaskSchedule(TaskSchedule taskSchedule) {
        return null;
    }
}
