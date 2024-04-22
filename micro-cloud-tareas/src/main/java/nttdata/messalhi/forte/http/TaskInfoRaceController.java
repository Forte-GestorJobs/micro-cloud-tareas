package nttdata.messalhi.forte.http;


import nttdata.messalhi.forte.entities.TaskInfo;
import nttdata.messalhi.forte.services.TaskInfoRaceService;
import nttdata.messalhi.forte.utils.DatabaseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("task")
public class TaskInfoRaceController {

    @Autowired
    private TaskInfoRaceService taskInfoRaceService;

    @PostMapping("/TaskInfo")
    public DatabaseResult addTaskInfo(@RequestBody TaskInfo taskInfo) {
        return this.taskInfoRaceService.addTaskInfo(taskInfo);
    }

    @GetMapping("/TaskInfo/{arn}")
    public DatabaseResult getTaskInfo(@PathVariable Long arn) {
        return this.taskInfoRaceService.getTaskInfo(arn);
    }

    @GetMapping("/TaskInfo/list/{userId}")
    public DatabaseResult listTaskInfo(@PathVariable String userId) {
        return this.taskInfoRaceService.listTaskInfo(userId);
    }

    @DeleteMapping("/TaskInfo/{arn}")
    public DatabaseResult deleteTaskInfo(@PathVariable Long arn) {
        return this.taskInfoRaceService.deleteTaskInfo(arn);
    }

    @PutMapping("/TaskInfo")
    public DatabaseResult updateTaskInfo(@RequestBody TaskInfo taskInfo) {
        return this.taskInfoRaceService.updateTaskInfo(taskInfo);
    }

}
