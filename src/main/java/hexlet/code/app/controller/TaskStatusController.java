package hexlet.code.app.controller;


import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.service.TaskStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.RelationException;
import javax.validation.Valid;
import java.util.List;

import static hexlet.code.app.controller.TaskStatusController.TASK_STATUS_CONTROLLER_PATH;

@Validated
@RestController
@RequestMapping("${base-url}" + TASK_STATUS_CONTROLLER_PATH)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TaskStatusController {

    public static final String TASK_STATUS_CONTROLLER_PATH = "/statuses";

    public static final String TASK_STATUS_ID = "/{id}";

    private final TaskStatusService taskStatusService;

    @GetMapping(path = TASK_STATUS_ID)
    public ResponseEntity<TaskStatus> getTaskStatusById(@PathVariable String id) {
        TaskStatus taskStatus = taskStatusService.getTaskStatusById(id);
        return ResponseEntity.ok().body(taskStatus);
    }

    @GetMapping(path = "")
    public ResponseEntity<List<TaskStatus>> getAllTaskStatuses() {
        List<TaskStatus> taskStatuses = taskStatusService.getAllTaskStatuses();
        return ResponseEntity.ok().body(taskStatuses);
    }

    @PostMapping(path = "")
    public ResponseEntity<TaskStatus> createTaskStatus(@RequestBody @Valid TaskStatus taskStatus) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskStatusService.createTaskStatus(taskStatus));
    }

    @PutMapping(path = TASK_STATUS_ID)
    public ResponseEntity<TaskStatus> updateTaskStatus(@PathVariable String id,
                                                       @RequestBody @Valid TaskStatus taskStatus) {
        TaskStatus updatedTaskStatus = taskStatusService.updateTaskStatus(id, taskStatus);
        return ResponseEntity.ok().body(updatedTaskStatus);
    }

    @DeleteMapping(path = TASK_STATUS_ID)
    public String deleteUser(@PathVariable String id) throws RelationException {
        return taskStatusService.deleteTaskStatus(id);
    }

}
