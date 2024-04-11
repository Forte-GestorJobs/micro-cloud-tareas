package nttdata.messalhi.forte.services;


import nttdata.messalhi.forte.auxi.AWSHelper;
import nttdata.messalhi.forte.dao.TaskDestinationDAO;
import nttdata.messalhi.forte.dao.TaskInfoDAO;
import nttdata.messalhi.forte.entities.TaskDestination;
import nttdata.messalhi.forte.entities.TaskInfo;
import nttdata.messalhi.forte.utils.DatabaseResult;
import nttdata.messalhi.forte.utils.ResultadoConsultaAWS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TaskInfoRaceService {
    @Autowired
    private TaskInfoDAO taskInfoDAO;

    @Autowired
    private TaskDestinationDAO taskDestinationDAO;
    public boolean existsTaskInfoNameAndUserID(String name, String userId) {
        Optional<TaskInfo> optUser = this.taskInfoDAO.findByNameAndUserId(name, userId);
        return optUser.isPresent();
    }

    public boolean existsTaskInfo(Long id) {
        Optional<TaskInfo> optUser = this.taskInfoDAO.findById(id);
        return optUser.isPresent();
    }
    public DatabaseResult addTaskInfo(TaskInfo taskInfo) {
        try{
            String name = taskInfo.getName();
            String userId = taskInfo.getUserId();
            String description = taskInfo.getDescription();
            String state = taskInfo.getState();

            if(existsTaskInfoNameAndUserID(name, userId)){
                return new DatabaseResult(false, "TaskInfo already exists");
            }
            else{
                TaskDestination target = taskDestinationDAO.findById(userId+"."+name).orElse(null);
                if (target == null){
                    return new DatabaseResult(false, "No se puede crear TaskInfo sin TaskDestination correspondiente para la tarea " + name);
                }
                else{
                    TaskInfo taskInfoDB = new TaskInfo(name, description, state, userId);
                    taskInfoDB.setTarget(target);
                    ResultadoConsultaAWS resultadoConsultaAWS = AWSHelper.crearSchedule(userId+"."+name, description, state, "rate(1 minute)"); //Sustituir la expresion
                    if (!resultadoConsultaAWS.isSuccess()){
                        return new DatabaseResult(false, resultadoConsultaAWS.getMessage());
                    }
                    else{
                        taskInfoDB.setArn(resultadoConsultaAWS.getMessage());
                        taskInfoDAO.save(taskInfoDB);
                        return new DatabaseResult(true, "TaskInfo " + taskInfoDB.getId() + " created");
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return new DatabaseResult(false, e.getMessage());
        }
    }

    public DatabaseResult getTaskInfo(Long id) {
        try{
            if (existsTaskInfo(id)) {
                TaskInfo taskInfo = this.taskInfoDAO.getReferenceById(id);
                return new DatabaseResult(true, taskInfo.toStringJSON()); // Operación exitosa
            }
            else{
                return new DatabaseResult(false, "TaskInfo with arn: " + id + " not found");
            }
        } catch (Exception e){
            e.printStackTrace();
            return new DatabaseResult(false, e.getMessage());
        }
    }

    public DatabaseResult listTaskInfo(String user_id) {
        return null;
    }

    public DatabaseResult deleteTaskInfo(Long id) {
        try {
            TaskInfo taskInfo = this.taskInfoDAO.getReferenceById(id);
            if (taskInfo != null) {
                this.taskDestinationDAO.deleteById(taskInfo.getTarget().getId());
                this.taskInfoDAO.deleteById(id);
            }
            return new DatabaseResult(true, "TaskInfo " + id +  " deleted");
        } catch (Exception e) {
            e.printStackTrace();
            return new DatabaseResult(false, e.getMessage());
        }
    }

    public DatabaseResult updateTaskInfo(TaskInfo taskInfo) {
        try{
            Long id = taskInfo.getId();
            String name = taskInfo.getName();
            String description = taskInfo.getDescription();
            String state = taskInfo.getState();
            if (existsTaskInfo(id)) {
                TaskInfo taskInfoDB = this.taskInfoDAO.getReferenceById(id);
                taskInfoDB.setName(name);
                taskInfoDB.setDescription(description);
                taskInfoDB.setState(state);
                this.taskInfoDAO.save(taskInfoDB);
                return new DatabaseResult(true, "TaskInfo " + id + " updated");
            }
            else{
                return new DatabaseResult(false, "TaskInfo " + id + " not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new DatabaseResult(false, e.getMessage());
        }
    }
}
