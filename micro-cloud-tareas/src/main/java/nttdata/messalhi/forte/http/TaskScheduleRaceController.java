package nttdata.messalhi.forte.http;


import nttdata.messalhi.forte.entities.TaskSchedule;
import nttdata.messalhi.forte.services.TaskScheduleRaceService;
import nttdata.messalhi.forte.utils.DatabaseResult;
import org.springframework.beans.factory.annotation.Autowired;
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
    public DatabaseResult getTaskSchedule(@PathVariable String id) {
        return this.taskScheduleRaceService.getTaskSchedule(id);
    }

    @GetMapping("/TaskSchedule/list/{taskId}")
    public DatabaseResult listTaskSchedule(@PathVariable String taskId) {
        return this.taskScheduleRaceService.listTaskSchedule(taskId);
    }

    @DeleteMapping("/TaskSchedule/{id}")
    public DatabaseResult deleteTaskSchedule(@PathVariable String id) {
        return this.taskScheduleRaceService.deleteTaskSchedule(id);
    }

    @PutMapping("/TaskSchedule")
    public DatabaseResult updateTaskSchedule(@RequestBody TaskSchedule taskSchedule) {
        return this.taskScheduleRaceService.updateTaskSchedule(taskSchedule);
    }

}
