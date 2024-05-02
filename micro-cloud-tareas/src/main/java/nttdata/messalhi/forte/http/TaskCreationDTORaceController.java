package nttdata.messalhi.forte.http;


import nttdata.messalhi.forte.auxi.TaskCreationDTO;
import nttdata.messalhi.forte.services.TaskCreationDTORaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("task")
public class TaskCreationDTORaceController {

    @Autowired
    private TaskCreationDTORaceService taskCreationDTORaceService;

    @PostMapping("/Task")
    public ResponseEntity<String> addTaskSchedule(@RequestBody TaskCreationDTO taskCreationDTO) {
        return this.taskCreationDTORaceService.addTask(taskCreationDTO);
    }
    @GetMapping("/Task/{id}")
    public ResponseEntity<String> getTaskSchedule(@PathVariable Long id) {
        return this.taskCreationDTORaceService.getTask(id);
    }

    @GetMapping("/Task/list/{user_id}")
    public ResponseEntity<String> listTaskByUserId(@PathVariable String user_id,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return this.taskCreationDTORaceService.listTaskSchedule(user_id, pageable);
    }
    @GetMapping("/Task/count/{user_id}")
    public ResponseEntity<String> countTasksByUserId(@PathVariable String user_id) {
        return this.taskCreationDTORaceService.countTasksByUserId(user_id);
    }
    @DeleteMapping("/Task/{id}")
    public ResponseEntity<String> deleteTaskSchedule(@PathVariable Long id) {
        return this.taskCreationDTORaceService.deleteTask(id);
    }
    @PutMapping("/Task/{id}")
    public ResponseEntity<String> updateTaskSchedule(@RequestBody TaskCreationDTO taskCreationDTO, @PathVariable Long id){
        return this.taskCreationDTORaceService.updateTask(taskCreationDTO, id);
    }

}
