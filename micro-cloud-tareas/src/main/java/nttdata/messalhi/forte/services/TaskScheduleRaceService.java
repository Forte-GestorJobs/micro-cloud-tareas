    package nttdata.messalhi.forte.services;


import nttdata.messalhi.forte.dao.TaskScheduleDAO;
import nttdata.messalhi.forte.entities.TaskSchedule;
import nttdata.messalhi.forte.utils.DatabaseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class TaskScheduleRaceService {
    private static String taskScheduleClass = "TaskSchedule ";
    @Autowired
    private TaskScheduleDAO taskScheduleDAO;

    public boolean existsTaskSchedule(String id) {
        Optional<TaskSchedule> optUser = this.taskScheduleDAO.findById(id);
        return optUser.isPresent();
    }

    public DatabaseResult addTaskSchedule(TaskSchedule taskSchedule) {
        try{
            String id = taskSchedule.getId();
            if (existsTaskSchedule(id)){
                return new DatabaseResult(false, taskScheduleClass + "already exists");
            }
            else{
                this.taskScheduleDAO.save(taskSchedule);
                return new DatabaseResult(true, taskScheduleClass + id + " added to the database.");
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
                return new DatabaseResult(false, taskScheduleClass + id + " not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new DatabaseResult(false, e.getMessage());
        }
    }

    public DatabaseResult listTaskSchedule(String userId) {
        return null;
    }

    public DatabaseResult deleteTaskSchedule(String id) {
        try {
            if (existsTaskSchedule(id)) {
                this.taskScheduleDAO.deleteById(id);
            }
            return new DatabaseResult(true, taskScheduleClass + id + " deleted");
        }catch (Exception e) {
            e.printStackTrace();
            return new DatabaseResult(false, e.getMessage());
        }
    }

    public DatabaseResult updateTaskSchedule(TaskSchedule taskSchedule) { //Es necesario??
        try {
            String id = taskSchedule.getId();
            Date startDate = taskSchedule.getStartDate();
            Date endDate = taskSchedule.getEndDate();
            String scheduleExpression = taskSchedule.getScheduleExpression();
            String timeZone = taskSchedule.getTimeZone();

            if (existsTaskSchedule(id)) {
                TaskSchedule taskScheduleDB = taskScheduleDAO.getReferenceById(id);
                taskScheduleDB.setStartDate(startDate);
                taskScheduleDB.setEndDate(endDate);
                taskScheduleDB.setScheduleExpression(scheduleExpression);
                taskScheduleDB.setTimeZone(timeZone);
                this.taskScheduleDAO.save(taskScheduleDB);

                return new DatabaseResult(true, taskScheduleClass + id + " updated.");
            } else {
                return new DatabaseResult(false, taskScheduleClass + id + " not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new DatabaseResult(false, e.getMessage());
        }
    }
}
