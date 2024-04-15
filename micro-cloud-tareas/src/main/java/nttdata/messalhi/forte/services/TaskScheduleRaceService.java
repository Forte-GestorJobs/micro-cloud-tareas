    package nttdata.messalhi.forte.services;


import nttdata.messalhi.forte.auxi.AWSHelper;
import nttdata.messalhi.forte.dao.TaskScheduleDAO;
import nttdata.messalhi.forte.entities.TaskDestination;
import nttdata.messalhi.forte.entities.TaskSchedule;
import nttdata.messalhi.forte.utils.DatabaseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class TaskScheduleRaceService {
    @Autowired
    private TaskScheduleDAO taskScheduleDAO;

    Logger logger = LoggerFactory.getLogger(TaskScheduleRaceService.class);
    public boolean existsTaskSchedule(String id) {
        Optional<TaskSchedule> optUser = this.taskScheduleDAO.findById(id);
        return optUser.isPresent();
    }

    public DatabaseResult addTaskSchedule(TaskSchedule taskSchedule) {
        try{
            String id = taskSchedule.getId();
            logger.error("TASK SCHEDULE ID: "+ id);
            logger.error("TASK SCHEDULE: "+ getTaskSchedule(id).getMessage());
            if (existsTaskSchedule(id)){
                return new DatabaseResult(false, "TaskSchedule already exists");
            }
            else{
                this.taskScheduleDAO.save(taskSchedule);
                return new DatabaseResult(true, "TaskSchedule " + id + " added to the database.");
            }
        }catch (Exception e){
            e.printStackTrace();
            return new DatabaseResult(false, e.getMessage());
        }
    }

    public DatabaseResult getTaskSchedule(String id) {
        try {
            if (existsTaskSchedule(id)) {
                TaskSchedule taskSchedule = this.taskScheduleDAO.getReferenceById(id);
                return new DatabaseResult(true, taskSchedule.toStringJSON()); // Operación exitosa
            }
            else{
                return new DatabaseResult(false, "TaskDestination " + id + " not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new DatabaseResult(false, e.getMessage());
        }
    }

    public DatabaseResult listTaskSchedule(String user_id) {
        return null;
    }

    public DatabaseResult deleteTaskSchedule(String id) {
        try {
            if (existsTaskSchedule(id)) {
                this.taskScheduleDAO.deleteById(id);
            }
            return new DatabaseResult(true, "TaskSchedule " + id + " deleted");
        }catch (Exception e) {
            e.printStackTrace();
            return new DatabaseResult(false, e.getMessage());
        }
    }

    public DatabaseResult updateTaskSchedule(TaskSchedule taskSchedule) { //Es necesario??
        try {
            String id = taskSchedule.getId();
            String type = taskSchedule.getType();
            Date startDate = taskSchedule.getStartDate();
            Date endDate = taskSchedule.getEndDate();
            Date creationDate = new Date();
            String scheduleExpression = taskSchedule.getScheduleExpression();
            String timeZone = taskSchedule.getTimeZone();

            if (existsTaskSchedule(id)) {
                TaskSchedule taskScheduleDB = taskScheduleDAO.getReferenceById(id);
                taskScheduleDB.setType(type);
                taskScheduleDB.setStartDate(startDate);
                taskScheduleDB.setEndDate(endDate);
                taskScheduleDB.setCreationDate(creationDate);
                taskScheduleDB.setScheduleExpression(scheduleExpression);
                taskScheduleDB.setTimeZone(timeZone);
                this.taskScheduleDAO.save(taskScheduleDB);

                return new DatabaseResult(true, "TaskSchedule " + id + " updated.");
            } else {
                return new DatabaseResult(false, "TaskSchedule " + id + " not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new DatabaseResult(false, e.getMessage());
        }
    }
}
