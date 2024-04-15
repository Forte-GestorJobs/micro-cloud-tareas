package nttdata.messalhi.forte.services;


import nttdata.messalhi.forte.auxi.AWSHelper;
import nttdata.messalhi.forte.dao.TaskDestinationDAO;
import nttdata.messalhi.forte.dao.TaskInfoDAO;
import nttdata.messalhi.forte.entities.TaskCreationDTO;
import nttdata.messalhi.forte.entities.TaskDestination;
import nttdata.messalhi.forte.entities.TaskInfo;
import nttdata.messalhi.forte.entities.TaskSchedule;
import nttdata.messalhi.forte.utils.DatabaseResult;
import nttdata.messalhi.forte.utils.ResultadoConsultaAWS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class TaskCreationDTORaceService {
    @Autowired
    TaskInfoRaceService taskInfoRaceService = new TaskInfoRaceService();

    @Autowired
    TaskScheduleRaceService taskScheduleRaceService = new TaskScheduleRaceService();
    @Autowired
    TaskDestinationRaceService taskDestinationRaceService = new TaskDestinationRaceService();


    @Autowired
    private TaskInfoDAO taskInfoDAO;

    Logger logger = LoggerFactory.getLogger(TaskScheduleRaceService.class);

    public boolean existsTaskInfo(Long id) {
        Optional<TaskInfo> optUser = this.taskInfoDAO.findById(id);
        return optUser.isPresent();
    }

    public DatabaseResult addTask(TaskCreationDTO taskCreationDTO) {
        try {
            TaskDestination taskDestination = new TaskDestination(taskCreationDTO.getUserId()+"."+taskCreationDTO.getName(), taskCreationDTO.getUrl(), taskCreationDTO.getHttpMethod(), taskCreationDTO.getBody());
            TaskInfo taskInfo = new TaskInfo(taskCreationDTO.getName(), taskCreationDTO.getDescription(), taskCreationDTO.getState(), taskCreationDTO.getUserId());
            TaskSchedule taskSchedule = new TaskSchedule(taskCreationDTO.getUserId()+"."+taskCreationDTO.getName(), taskCreationDTO.getType(), taskCreationDTO.getStartDate(), taskCreationDTO.getEndDate(), taskCreationDTO.getScheduleExpression(), taskCreationDTO.getTimeZone());
            taskInfo.setTarget(taskDestination);
            taskInfo.setSchedule(taskSchedule);
            taskSchedule.setTaskInfo(taskInfo);

            // Guardar AWS Schedule
            ResultadoConsultaAWS resultadoConsultaAWS = AWSHelper.createSchedule(taskCreationDTO.getUserId() + "." + taskCreationDTO.getName(), taskCreationDTO.getDescription(), taskCreationDTO.getState(), taskCreationDTO.getScheduleExpression());
            if (!resultadoConsultaAWS.isSuccess()) {
                return new DatabaseResult(false, "Error creating AWS Schedule: " + resultadoConsultaAWS.getMessage());
            }

            // Guardar TaskInfo TaskSchedule y TaskDestination
            taskInfo.setArn(resultadoConsultaAWS.getMessage());
            DatabaseResult resultTI = taskInfoRaceService.addTaskInfo(taskInfo);
            if (!resultTI.isSuccess()) {
                return new DatabaseResult(false, "Error creating TaskInfo: " + resultTI.getMessage());
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

            return new DatabaseResult(true, "Task " + taskInfo.getId() + " created successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return new DatabaseResult(false, "Error: " + e.getMessage());
        }
    }


    public DatabaseResult getTask(Long id){
        try{
            if (existsTaskInfo(id)) {
                TaskInfo taskInfo = this.taskInfoDAO.getReferenceById(id);
                //TaskDestination taskDestination = taskInfo.getTarget();
                return new DatabaseResult(true, taskInfo.toStringJSON()); // Operación exitosa
            }
            else{
                return new DatabaseResult(false, "Task with arn: " + id + " not found");
            }
        } catch (Exception e){
            e.printStackTrace();
            return new DatabaseResult(false, e.getMessage());

        }
    }
    public DatabaseResult deleteTask(Long id) {

        try {
            if (existsTaskInfo(id)) {
                TaskInfo taskInfo = this.taskInfoDAO.getReferenceById(id);
                ResultadoConsultaAWS resultadoConsultaAWS = AWSHelper.deleteSchedule(taskInfo.getUserId()+ "." + taskInfo.getName());
                if (!resultadoConsultaAWS.isSuccess()) {
                    return new DatabaseResult(false, "Error deleting AWS Schedule: " + resultadoConsultaAWS.getMessage());
                }
                this.taskInfoDAO.deleteById(id);
                return new DatabaseResult(true, "Task " + id + " deleted");
            }
            return new DatabaseResult(true, "Task " + id + " deleted");
        }catch (Exception e) {
            e.printStackTrace();
            return new DatabaseResult(false, e.getMessage());
        }

    }

    public DatabaseResult updateTask(TaskCreationDTO taskCreationDTO, Long taskId) {
        try {
            // Buscar la tarea existente por su ID
            TaskInfo taskInfo = taskInfoDAO.getReferenceById(taskId);
            if (taskInfo == null) {
                return new DatabaseResult(false, "Task not found with ID: " + taskId);
            }
            taskInfo.setDescription(taskCreationDTO.getDescription());
            taskInfo.setState(taskCreationDTO.getState());

            TaskDestination taskDestination = taskInfo.getTarget();
            taskDestination.setUrl(taskCreationDTO.getUrl());
            taskDestination.setHttpMethod(taskCreationDTO.getHttpMethod());
            taskDestination.setBody(taskCreationDTO.getBody());

            TaskSchedule taskSchedule = taskInfo.getSchedule();
            taskSchedule.setScheduleExpression(taskCreationDTO.getScheduleExpression());
            taskSchedule.setType(taskCreationDTO.getType());
            taskSchedule.setStartDate(taskCreationDTO.getStartDate());
            taskSchedule.setEndDate(taskCreationDTO.getEndDate());
            taskSchedule.setTimeZone(taskCreationDTO.getTimeZone());

            ResultadoConsultaAWS resultadoConsultaAWS = AWSHelper.updateSchedule(taskInfo.getUserId()+"."+taskInfo.getName(), taskCreationDTO.getDescription(), taskCreationDTO.getState(),taskCreationDTO.getScheduleExpression());
            logger.info("Resultado update AWS: "+ resultadoConsultaAWS.getMessage());
            if (!resultadoConsultaAWS.isSuccess()) {
                return new DatabaseResult(false, "Error updating AWS Schedule: " + resultadoConsultaAWS.getMessage());
            }
            DatabaseResult result = taskInfoRaceService.updateTaskInfo(taskInfo);
            if (!result.isSuccess()) {
                return new DatabaseResult(false, "Error updating Task: " + result.getMessage());
            }

            return new DatabaseResult(true, "Task " + taskId + " updated successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return new DatabaseResult(false, "Error: " + e.getMessage());
        }
    }



}
