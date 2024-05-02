package nttdata.messalhi.forte.services;


import nttdata.messalhi.forte.auxi.AWSHelper;
import nttdata.messalhi.forte.dao.TaskInfoDAO;
import nttdata.messalhi.forte.auxi.TaskCreationDTO;
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
    private TaskInfoDAO taskInfoDAO;

    Logger logger = LoggerFactory.getLogger(TaskCreationDTORaceService.class);

    public boolean existsTaskInfo(Long id) {
        Optional<TaskInfo> optUser = this.taskInfoDAO.findById(id);
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
            TaskDestination taskDestination = new TaskDestination(taskCreationDTO.getUserId()+"."+taskCreationDTO.getName(), taskCreationDTO.getUrl(), taskCreationDTO.getHttpMethod(), taskCreationDTO.getBody());
            TaskInfo taskInfo = new TaskInfo(taskCreationDTO.getName(), taskCreationDTO.getDescription(), taskCreationDTO.getState(), taskCreationDTO.getUserId());
            TaskSchedule taskSchedule = new TaskSchedule(taskCreationDTO.getUserId()+"."+taskCreationDTO.getName(), taskCreationDTO.getStartDate(), taskCreationDTO.getEndDate(), taskCreationDTO.getScheduleExpression(), taskCreationDTO.getTimeZone(), taskCreationDTO.getMaximumTimeWindowInMinutes());
            taskInfo.setDestination(taskDestination);
            taskInfo.setSchedule(taskSchedule);
            taskSchedule.setTaskInfo(taskInfo);
            // Guardar AWS Schedule
            ResultadoConsultaAWS resultadoConsultaAWS = AWSHelper.createSchedule(taskInfo, buildInputAWS(taskInfo));
            if (!resultadoConsultaAWS.isSuccess()) {
                return new DatabaseResult(false, "Error creating AWS Schedule: " + resultadoConsultaAWS.getMessage()).response();
            }

            // Guardar TaskInfo TaskSchedule y TaskDestination
            taskInfo.setArn(resultadoConsultaAWS.getMessage());
            DatabaseResult resultTI = taskInfoRaceService.addTaskInfo(taskInfo);
            if (!resultTI.isSuccess()) {
                return new DatabaseResult(false, "Error creating TaskInfo: " + resultTI.getMessage()).response();
            }

            // Guardar TaskDestination
            //DatabaseResult resultTD = taskDestinationRaceService.addTaskDestination(taskDestination);
            //if (!resultTD.isSuccess()) {
            //    return new DatabaseResult(false, "Error creating TaskDestination: " + resultTD.getMessage());
            //}

            // Crear TaskSchedule
            //DatabaseResult resultTS = taskScheduleRaceService.addTaskSchedule(taskSchedule);
            //if (!resultTS.isSuccess()) {
            //    return new DatabaseResult(false, "Error creating TaskSchedule: " + resultTS.getMessage());
            //}

            return new DatabaseResult(true, taskClass + taskInfo.getId() + " created successfully").response();
        } catch (Exception e) {
            e.printStackTrace();
            return new DatabaseResult(false, "Error: " + e.getMessage()).response();
        }
    }


    public ResponseEntity<String> getTask(Long id){
        try{
            if (existsTaskInfo(id)) {
                TaskInfo taskInfo = this.taskInfoDAO.getReferenceById(id);
                return new DatabaseResult(true, taskInfo.toStringJSON()).response(); // Operación exitosa
            }
            else{
                return new DatabaseResult(false, taskClass + "with arn: " + id + " not found").response();
            }
        } catch (Exception e){
            e.printStackTrace();
            return new DatabaseResult(false, e.getMessage()).response();

        }
    }

    public ResponseEntity<String> listTaskSchedule(String task_id, Pageable pageable) {
        try {
            Page<TaskInfo> taskPage = this.taskInfoDAO.findByUserId(task_id, pageable);
            if (taskPage.isEmpty()) {
                return new DatabaseResult(false, "No tasks found for user: " + task_id).response();
            } else {
                List<String> taskList = new ArrayList<>();
                for (TaskInfo task : taskPage.getContent()) {
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
            long count = this.taskInfoDAO.countByUserId(user_id);
            return new DatabaseResult(true, String.valueOf(count)).response();
        } catch (Exception e) {
            e.printStackTrace();
            return new DatabaseResult(false, e.getMessage()).response();
        }
    }

    public ResponseEntity<String> deleteTask(Long id) {

        try {
            if (existsTaskInfo(id)) {
                TaskInfo taskInfo = this.taskInfoDAO.getReferenceById(id);
                ResultadoConsultaAWS resultadoConsultaAWS = AWSHelper.deleteSchedule(taskInfo.getUserId()+ "." + taskInfo.getName());
                if (!resultadoConsultaAWS.isSuccess()) {
                    return new DatabaseResult(false, "Error deleting AWS Schedule: " + resultadoConsultaAWS.getMessage()).response();
                }
                this.taskInfoDAO.deleteById(id);
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
            if (!existsTaskInfo(taskId)) {
                return new DatabaseResult(false, "Task not found with ID: " + taskId).response();
            }
            else{
                if (taskCreationDTO.getMaximumTimeWindowInMinutes()>1440){
                    return new DatabaseResult(false, "El maximumTimeWindow debe ser menor a 1440 minutos").response();
                }
                if (!validTimeZones.contains(taskCreationDTO.getTimeZone())){
                    return new DatabaseResult(false, "Invalid TimeZone: " + taskCreationDTO.getTimeZone()).response();
                }
                TaskInfo taskInfo = taskInfoDAO.getReferenceById(taskId);
                taskInfo.setDescription(taskCreationDTO.getDescription());
                taskInfo.setState(taskCreationDTO.getState());

                TaskDestination taskDestination = taskInfo.getDestination();
                taskDestination.setUrl(taskCreationDTO.getUrl());
                taskDestination.setHttpMethod(taskCreationDTO.getHttpMethod());
                taskDestination.setBody(taskCreationDTO.getBody());

                TaskSchedule taskSchedule = taskInfo.getSchedule();
                taskSchedule.setScheduleExpression(taskCreationDTO.getScheduleExpression());
                taskSchedule.setStartDate(taskCreationDTO.getStartDate());
                taskSchedule.setEndDate(taskCreationDTO.getEndDate());
                taskSchedule.setTimeZone(taskCreationDTO.getTimeZone());
                taskSchedule.setMaximumTimeWindowInMinutes(taskCreationDTO.getMaximumTimeWindowInMinutes());

                ResultadoConsultaAWS resultadoConsultaAWS = AWSHelper.updateSchedule(taskInfo.getUserId()+"."+taskInfo.getName(), taskCreationDTO.getDescription(), taskCreationDTO.getState(),taskCreationDTO.getScheduleExpression(), taskCreationDTO.getMaximumTimeWindowInMinutes());
                logger.debug(String.format("Resultado update AWS: %s", resultadoConsultaAWS.getMessage()));
                if (!resultadoConsultaAWS.isSuccess()) {
                    return new DatabaseResult(false, "Error updating AWS Schedule: " + resultadoConsultaAWS.getMessage()).response();
                }
                DatabaseResult result = taskInfoRaceService.updateTaskInfo(taskInfo);
                if (!result.isSuccess()) {
                    return new DatabaseResult(false, "Error updating Task: " + result.getMessage()).response();
                }

                return new DatabaseResult(true, "Task " + taskId + " updated successfully").response();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new DatabaseResult(false, "Error: " + e.getMessage()).response();
        }
    }



    public String buildInputAWS(TaskInfo taskInfo) throws JsonProcessingException {
        // Crear un objeto ObjectMapper para convertir objetos Java a JSON
        ObjectMapper objectMapper = new ObjectMapper();

        // Construir un mapa para representar el JSON
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("user_id", taskInfo.getName());
        jsonMap.put("schedule_id", taskInfo.getUserId() + "." + taskInfo.getName());
        jsonMap.put("url", taskInfo.getDestination().getUrl());
        jsonMap.put("http_method", taskInfo.getDestination().getHttpMethod());
        jsonMap.put("body", taskInfo.getDestination().getBody());

        // Convertir la fecha a una cadena en el formato deseado
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String dateString = dateFormat.format(new Date());
        jsonMap.put("date", dateString);

        // Convertir el mapa a una cadena JSON
        String jsonInput = objectMapper.writeValueAsString(jsonMap);

        return jsonInput;
    }



}
