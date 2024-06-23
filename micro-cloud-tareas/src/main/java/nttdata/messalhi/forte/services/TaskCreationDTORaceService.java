package nttdata.messalhi.forte.services;


import nttdata.messalhi.forte.auxi.AWSHelper;
import nttdata.messalhi.forte.dao.TaskDAO;
import nttdata.messalhi.forte.dao.TaskInfoDAO;
import nttdata.messalhi.forte.auxi.TaskCreationDTO;
import nttdata.messalhi.forte.entities.Task;
import nttdata.messalhi.forte.entities.TaskDestination;
import nttdata.messalhi.forte.entities.TaskInfo;
import nttdata.messalhi.forte.entities.TaskSchedule;
import nttdata.messalhi.forte.utils.DatabaseResult;
import nttdata.messalhi.forte.utils.ResultadoConsultaAWS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class TaskCreationDTORaceService {
    private static String taskClass = "Task ";
    @Autowired
    TaskInfoRaceService taskInfoRaceService = new TaskInfoRaceService();

    @Autowired
    TaskScheduleRaceService taskScheduleRaceService = new TaskScheduleRaceService();
    @Autowired
    TaskDestinationRaceService taskDestinationRaceService = new TaskDestinationRaceService();


    @Autowired
    private TaskDAO taskDAO;

    Logger logger = LoggerFactory.getLogger(TaskCreationDTORaceService.class);

    public boolean existsTask(Long id) {
        Optional<Task> optUser = this.taskDAO.findById(id);
        return optUser.isPresent();
    }

    public ResponseEntity<String> addTask(TaskCreationDTO taskCreationDTO) {
        List<String> validTimeZones = Arrays.asList(
                "Europe/London", "Europe/Madrid", "Europe/Paris", "Europe/Berlin",
                "Europe/Moscow", "America/New_York", "America/Los_Angeles",
                "America/Sao_Paulo", "Asia/Tokyo", "Asia/Shanghai"
        );

        try {
            if (taskCreationDTO.getMaximumTimeWindowInMinutes()>1440){
                return new DatabaseResult(false, "El maximumTimeWindow debe ser menor a 1440 minutos").response();
            }
            if (!validTimeZones.contains(taskCreationDTO.getTimeZone())){
                return new DatabaseResult(false, "Invalid TimeZone: " + taskCreationDTO.getTimeZone()).response();
            }
            TaskDestination taskDestination = new TaskDestination(taskCreationDTO.getUserId()+"."+taskCreationDTO.getName(), taskCreationDTO.getUrl(), taskCreationDTO.getHttpMethod(), taskCreationDTO.getBody(), 1);
            TaskInfo taskInfo = new TaskInfo(taskCreationDTO.getDescription(), taskCreationDTO.getState(), 1);
            TaskSchedule taskSchedule = new TaskSchedule(taskCreationDTO.getUserId()+"."+taskCreationDTO.getName(), taskCreationDTO.getStartDate(), taskCreationDTO.getEndDate(), taskCreationDTO.getScheduleExpression(), taskCreationDTO.getTimeZone(), taskCreationDTO.getMaximumTimeWindowInMinutes(), 1);
            

            if (taskDAO.findByNameAndUserId(taskCreationDTO.getName(), taskCreationDTO.getUserId()).isPresent()){
                return new DatabaseResult(false, "Ya existe una tarea con ese nombre para ese usuario").response();
            }

            Task task = new Task(taskCreationDTO.getName(), taskCreationDTO.getUserId());
            task.addTaskDestination(taskDestination);
            task.addTaskInfo(taskInfo);
            task.addTaskSchedule(taskSchedule);

            ResultadoConsultaAWS resultadoConsultaAWS = AWSHelper.createSchedule(task, buildInputAWS(task));
            if (!resultadoConsultaAWS.isSuccess()) {
                return new DatabaseResult(false, "Error creando la tarea en AWS: " + resultadoConsultaAWS.getMessage()).response();
            }
            task.setArn(resultadoConsultaAWS.getMessage());
            taskDAO.save(task);
            return new DatabaseResult(true, taskClass + task.getId() + " created successfully").response();
        } catch (Exception e) {
            e.printStackTrace();
            return new DatabaseResult(false, "Error: " + e.getMessage()).response();
        }
    }


    public ResponseEntity<String> getTask(Long id){
        try{
            if (existsTask(id)) {
                Task task = this.taskDAO.getReferenceById(id);
                return new DatabaseResult(true, task.toStringJSON()).response(); 
            }
            else{
                return new DatabaseResult(false, taskClass + "with id: " + id + " not found").response();
            }
        } catch (Exception e){
            e.printStackTrace();
            return new DatabaseResult(false, e.getMessage()).response();

        }
    }

    public ResponseEntity<String> listTaskSchedule(String task_id, Pageable pageable) {
        try {
            Page<Task> taskPage = this.taskDAO.findByUserId(task_id, pageable);
            if (taskPage.isEmpty()) {
                return new DatabaseResult(false, "No tasks found for user: " + task_id).response();
            } else {
                List<String> taskList = new ArrayList<>();
                for (Task task : taskPage.getContent()) {
                    taskList.add(task.toStringJSON());
                }
                return new DatabaseResult(true, taskList.toString()).response();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new DatabaseResult(false, e.getMessage()).response();
        }
    }

    public ResponseEntity<String> countTasksByUserId(String user_id) {
        try {
            long count = this.taskDAO.countByUserId(user_id);
            return new DatabaseResult(true, String.valueOf(count)).response();
        } catch (Exception e) {
            e.printStackTrace();
            return new DatabaseResult(false, e.getMessage()).response();
        }
    }

    public ResponseEntity<String> deleteTask(Long id) {

        try {
            if (existsTask(id)) {
                Task task = this.taskDAO.getReferenceById(id);
                ResultadoConsultaAWS resultadoConsultaAWS = AWSHelper.deleteSchedule(task.getUserId()+ "." + task.getName());
                if (!resultadoConsultaAWS.isSuccess()) {
                    return new DatabaseResult(false, "Error deleting AWS Schedule: " + resultadoConsultaAWS.getMessage()).response();
                }
                this.taskDAO.deleteById(id);
                return new DatabaseResult(true, taskClass + id + " deleted").response();
            }
            return new DatabaseResult(true, taskClass + id + " deleted").response();
        }catch (Exception e) {
            e.printStackTrace();
            return new DatabaseResult(false, e.getMessage()).response();
        }

    }

    public ResponseEntity<String> updateTask(TaskCreationDTO taskCreationDTO, Long taskId) {
        List<String> validTimeZones = Arrays.asList(
                "Europe/London", "Europe/Madrid", "Europe/Paris", "Europe/Berlin",
                "Europe/Moscow", "America/New_York", "America/Los_Angeles",
                "America/Sao_Paulo", "Asia/Tokyo", "Asia/Shanghai"
        );
        try {
            // Buscar la tarea existente por su ID
            if (!existsTask(taskId)) {
                return new DatabaseResult(false, "Task not found with ID: " + taskId).response();
            }
            else{
                if (taskCreationDTO.getMaximumTimeWindowInMinutes()>1440){
                    return new DatabaseResult(false, "El maximumTimeWindow debe ser menor a 1440 minutos").response();
                }
                if (!validTimeZones.contains(taskCreationDTO.getTimeZone())){
                    return new DatabaseResult(false, "Invalid TimeZone: " + taskCreationDTO.getTimeZone()).response();
                }
                Task task = taskDAO.getReferenceById(taskId);
                int version = generateVersion(task);
                TaskDestination taskDestination = new TaskDestination(taskCreationDTO.getUserId()+"."+taskCreationDTO.getName(), taskCreationDTO.getUrl(), taskCreationDTO.getHttpMethod(), taskCreationDTO.getBody(), version);
                TaskInfo taskInfo = new TaskInfo(taskCreationDTO.getDescription(), taskCreationDTO.getState(), version);
                TaskSchedule taskSchedule = new TaskSchedule(taskCreationDTO.getUserId()+"."+taskCreationDTO.getName(), taskCreationDTO.getStartDate(), taskCreationDTO.getEndDate(), taskCreationDTO.getScheduleExpression(), taskCreationDTO.getTimeZone(), taskCreationDTO.getMaximumTimeWindowInMinutes(), version);
                
                task.addTaskDestination(taskDestination);
                task.addTaskInfo(taskInfo);
                task.addTaskSchedule(taskSchedule);
                

                ResultadoConsultaAWS resultadoConsultaAWS = AWSHelper.updateSchedule(task.getUserId()+"."+task.getName(), taskCreationDTO.getDescription(), taskCreationDTO.getState(),taskCreationDTO.getScheduleExpression(), taskCreationDTO.getMaximumTimeWindowInMinutes());
                logger.debug(String.format("Resultado update AWS: %s", resultadoConsultaAWS.getMessage()));
                if (!resultadoConsultaAWS.isSuccess()) {
                    return new DatabaseResult(false, "Error updating AWS Schedule: " + resultadoConsultaAWS.getMessage()).response();
                }
                taskDAO.save(task);
                return new DatabaseResult(true, "Task " + taskId + " updated successfully").response();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new DatabaseResult(false, "Error: " + e.getMessage()).response();
        }
    }



    public String buildInputAWS(Task task) throws JsonProcessingException {
        // Crear un objeto ObjectMapper para convertir objetos Java a JSON
        ObjectMapper objectMapper = new ObjectMapper();
        TaskDestination taskDestination = task.getTaskDestination().get(task.getTaskDestination().size()-1);
        // Construir un mapa para representar el JSON
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("user_id", task.getName());
        jsonMap.put("schedule_id", task.getUserId() + "." + task.getName());
        jsonMap.put("url", taskDestination.getUrl());
        jsonMap.put("http_method", taskDestination.getHttpMethod());
        jsonMap.put("body", taskDestination.getBody());

        // Convertir la fecha a una cadena en el formato deseado
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String dateString = dateFormat.format(new Date());
        jsonMap.put("date", dateString);

        // Convertir el mapa a una cadena JSON
        String jsonInput = objectMapper.writeValueAsString(jsonMap);

        return jsonInput;
    }
    public int generateVersion(Task task){
        int actual_version = task.getTaskInfo().get(task.getTaskInfo().size()-1).getVersion();
        return actual_version+1;
    }
}
