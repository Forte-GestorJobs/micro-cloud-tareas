package nttdata.messalhi.forte.http;


import nttdata.messalhi.forte.entities.TaskCreationDTO;
import nttdata.messalhi.forte.services.TaskCreationDTORaceService;
import nttdata.messalhi.forte.utils.DatabaseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("task")
public class TaskCreationDTORaceController {

    @Autowired
    private TaskCreationDTORaceService taskCreationDTORaceService;

    @PostMapping("/Task")
    public DatabaseResult addTaskSchedule(@RequestBody TaskCreationDTO taskCreationDTO) {
        return this.taskCreationDTORaceService.addTask(taskCreationDTO);
    }

    @GetMapping("/Task/{id}")
    public DatabaseResult getTaskSchedule(@PathVariable Long id) {
        return this.taskCreationDTORaceService.getTask(id);
    }
/*
    @GetMapping("/TaskSchedule/list/{task_id}")
    public DatabaseResult listTaskSchedule(@PathVariable String task_id) {
        return this.taskCreationDTORaceService.listTaskSchedule(task_id);
    }
*/
    @DeleteMapping("/Task/{id}")
    public DatabaseResult deleteTaskSchedule(@PathVariable Long id) {
        return this.taskCreationDTORaceService.deleteTask(id);
    }

    @PutMapping("/Task/{id}")
    public DatabaseResult updateTaskSchedule(@RequestBody TaskCreationDTO taskCreationDTO, @PathVariable Long id){
        return this.taskCreationDTORaceService.updateTask(taskCreationDTO, id);
    }

}
