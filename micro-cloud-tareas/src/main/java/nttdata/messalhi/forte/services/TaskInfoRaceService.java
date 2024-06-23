package nttdata.messalhi.forte.services;


import nttdata.messalhi.forte.auxi.AWSHelper;
import nttdata.messalhi.forte.dao.TaskDestinationDAO;
import nttdata.messalhi.forte.dao.TaskInfoDAO;
import nttdata.messalhi.forte.entities.TaskInfo;
import nttdata.messalhi.forte.utils.DatabaseResult;
import nttdata.messalhi.forte.utils.ResultadoConsultaAWS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TaskInfoRaceService {
    private static String taskInfoClass = "TaskInfo ";
    @Autowired
    private TaskInfoDAO taskInfoDAO;

    @Autowired
    private TaskDestinationDAO taskDestinationDAO;
    public boolean existsTaskInfoUserID(Long id) {
        Optional<TaskInfo> optUser = this.taskInfoDAO.findById(id);
        return optUser.isPresent();
    }

    public boolean existsTaskInfo(Long id) {
        Optional<TaskInfo> optUser = this.taskInfoDAO.findById(id);
        return optUser.isPresent();
    }
    public DatabaseResult addTaskInfo(TaskInfo taskInfo) {
        try{
            Long id = taskInfo.getId();
            if(existsTaskInfoUserID(id)){
                return new DatabaseResult(false, taskInfoClass + "already exists");
            }
            else{
                taskInfoDAO.save(taskInfo);
                return new DatabaseResult(true, taskInfoClass + taskInfo.getId() + " created");
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
                return new DatabaseResult(false, taskInfoClass + " with arn: " + id + " not found");
            }
        } catch (Exception e){
            e.printStackTrace();
            return new DatabaseResult(false, e.getMessage());
        }
    }

    public DatabaseResult listTaskInfo(String userId) {
        return null;
    }

    public DatabaseResult deleteTaskInfo(Long id) {
        try {

            if (existsTaskInfo(id)) {
                TaskInfo taskInfo = this.taskInfoDAO.getReferenceById(id);
                this.taskInfoDAO.deleteById(id);
            }
            return new DatabaseResult(true, taskInfoClass + id +  " deleted");
        } catch (Exception e) {
            e.printStackTrace();
            return new DatabaseResult(false, e.getMessage());
        }
    }

    public DatabaseResult updateTaskInfo(TaskInfo taskInfo) {
        try{
            Long id = taskInfo.getId();
            String description = taskInfo.getDescription();
            String state = taskInfo.getState();
            if (existsTaskInfo(id)) {
                TaskInfo taskInfoDB = this.taskInfoDAO.getReferenceById(id);
                taskInfoDB.setDescription(description);
                taskInfoDB.setState(state);
                this.taskInfoDAO.save(taskInfoDB);
                return new DatabaseResult(true, taskInfoClass + id + " updated");
            }
            else{
                return new DatabaseResult(false, taskInfoClass + id + " not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new DatabaseResult(false, e.getMessage());
        }
    }
}
