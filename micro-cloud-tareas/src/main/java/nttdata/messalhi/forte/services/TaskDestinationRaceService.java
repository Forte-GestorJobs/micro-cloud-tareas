package nttdata.messalhi.forte.services;


import nttdata.messalhi.forte.dao.TaskDestinationDAO;
import nttdata.messalhi.forte.entities.TaskDestination;
import nttdata.messalhi.forte.utils.DatabaseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TaskDestinationRaceService {
    private static String taskDestinationClass = "TaskDestination ";
    @Autowired
    private TaskDestinationDAO taskDestinationDAO;
    public boolean existsTaskDestination(Long id) {
        Optional<TaskDestination> optUser = this.taskDestinationDAO.findById(id);
        return optUser.isPresent();
    }
    public DatabaseResult addTaskDestination(TaskDestination taskDestination) {
        try{
            Long id = taskDestination.getId();
            if (existsTaskDestination(id)){
                return new DatabaseResult(false, taskDestinationClass +"already exists");
            }
            else{
                this.taskDestinationDAO.save(taskDestination);
                return new DatabaseResult(true, taskDestinationClass + id + " added to the database.");
            }
        }catch (Exception e){
            e.printStackTrace();
            return new DatabaseResult(false, e.getMessage());
        }
    }

    public DatabaseResult getTaskDestination(Long id) {
        try {
            if (existsTaskDestination(id)) {
                TaskDestination taskDestination = this.taskDestinationDAO.getReferenceById(id);
                return new DatabaseResult(true, taskDestination.toStringJSON()); // Operación exitosa
            }
            else{
                return new DatabaseResult(false, taskDestinationClass + id + " not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new DatabaseResult(false, e.getMessage());
        }
    }
    
    public DatabaseResult deleteTaskDestination(Long id) {
        try {
            if (existsTaskDestination(id)) {
                this.taskDestinationDAO.deleteById(id);
            }
            return new DatabaseResult(true, taskDestinationClass + id + " deleted");
        }catch (Exception e) {
            e.printStackTrace();
            return new DatabaseResult(false, e.getMessage());
        }
    }

    public DatabaseResult updateTaskDestination(TaskDestination taskDestination) {
        try {
            Long id = taskDestination.getId();
            String url = taskDestination.getUrl();
            String httpMethod = taskDestination.getHttpMethod();
            String body = taskDestination.getBody();

            if (existsTaskDestination(id)) {
                TaskDestination taskDestinationDB = taskDestinationDAO.getReferenceById(id);
                taskDestinationDB.setUrl(url);
                taskDestinationDB.setHttpMethod(httpMethod);
                taskDestinationDB.setBody(body);
                this.taskDestinationDAO.save(taskDestinationDB);
                return new DatabaseResult(true, taskDestinationClass + id + " updated.");

            } else {
                return new DatabaseResult(false, taskDestinationClass + id + " not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new DatabaseResult(false, e.getMessage());
        }
    }
}
