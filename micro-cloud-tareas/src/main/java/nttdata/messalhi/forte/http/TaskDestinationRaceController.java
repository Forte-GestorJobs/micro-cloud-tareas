package nttdata.messalhi.forte.http;


import nttdata.messalhi.forte.entities.TaskDestination;
import nttdata.messalhi.forte.services.TaskDestinationRaceService;
import nttdata.messalhi.forte.utils.DatabaseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("task")
public class TaskDestinationRaceController {

    @Autowired
    private TaskDestinationRaceService taskDestinationRaceService;

    @PostMapping("/TaskDestination")
    public DatabaseResult addTaskDestination(@RequestBody TaskDestination taskDestination) {
        return this.taskDestinationRaceService.addTaskDestination(taskDestination);
    }

    @GetMapping("/TaskDestination/{id}")
    public DatabaseResult getTaskDestination(@PathVariable String id) {
        return this.taskDestinationRaceService.getTaskDestination(id);
    }
    @DeleteMapping("/TaskDestination/{id}")
    public DatabaseResult deleteTaskDestination(@PathVariable String id) {
        return this.taskDestinationRaceService.deleteTaskDestination(id);
    }
    @PutMapping("/TaskDestination")
    public DatabaseResult updateTaskDestination(@RequestBody TaskDestination taskDestination) {
        return this.taskDestinationRaceService.updateTaskDestination(taskDestination);
    }

}
