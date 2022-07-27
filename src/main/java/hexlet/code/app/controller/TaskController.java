package hexlet.code.app.controller;

import hexlet.code.app.model.Task;
import hexlet.code.app.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static hexlet.code.app.controller.TaskController.TASK_CONTROLLER_PATH;

@Validated
@RestController
@RequestMapping("${base-url}" + TASK_CONTROLLER_PATH)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TaskController {

    public static final String TASK_CONTROLLER_PATH = "/tasks";

    public static final String TASK_ID = "/{id}";

    private final TaskService taskService;

    @GetMapping(path = TASK_ID)
    public ResponseEntity<Task> getTaskById(@PathVariable String id) {
        Task task = taskService.getTaskById(id);
        return ResponseEntity.ok().body(task);
    }

    @GetMapping(path = "")
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasks = taskService.getAllTasks();
        return ResponseEntity.ok().body(tasks);
    }

    @PostMapping(path = "")
    public ResponseEntity<Task> createTask(@RequestBody @Valid Task task) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(task));
    }

    @PutMapping(path = TASK_ID)
    public ResponseEntity<Task> updateTask(@PathVariable String id,
                                           @RequestBody @Valid Task task) {
        Task updatedTask = taskService.updateTask(id, task);
        return ResponseEntity.ok().body(updatedTask);
    }

    @DeleteMapping(path = TASK_ID)
    public String deleteUser(@PathVariable String id) {
        return taskService.deleteTask(id);
    }

}
