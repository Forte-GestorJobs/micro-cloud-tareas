package nttdata.messalhi.forte.http;


import nttdata.messalhi.forte.entities.TaskSchedule;
import nttdata.messalhi.forte.services.TaskScheduleRaceService;
import nttdata.messalhi.forte.utils.DatabaseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("task")
public class TaskScheduleRaceController {

    @Autowired
    private TaskScheduleRaceService taskScheduleRaceService;

    @PostMapping("/TaskSchedule")
    public DatabaseResult addTaskSchedule(@RequestBody TaskSchedule taskSchedule) {
        return this.taskScheduleRaceService.addTaskSchedule(taskSchedule);
    }

    @GetMapping("/TaskSchedule/{id}")
    public ResponseEntity<String> getTaskSchedule(@PathVariable String arn) {
        return this.taskScheduleRaceService.getTaskSchedule(arn);
    }

    @GetMapping("/TaskSchedule/list/{task_id}")
    public ResponseEntity<String> listTaskSchedule(@PathVariable String user_id) {
        return this.taskScheduleRaceService.listTaskSchedule(user_id);
    }

    @DeleteMapping("/TaskSchedule/{id}")
    public ResponseEntity<String> deleteTaskSchedule(@PathVariable String arn) {
        return this.taskScheduleRaceService.deleteTaskSchedule(arn);
    }

    @PutMapping("/TaskSchedule")
    public ResponseEntity<String> updateTaskSchedule(@RequestBody TaskSchedule taskSchedule) {
        return this.taskScheduleRaceService.updateTaskSchedule(taskSchedule);
    }

}
