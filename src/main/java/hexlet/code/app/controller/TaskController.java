package hexlet.code.app.controller;

import hexlet.code.app.model.DTO.TaskRequestDTO;
import hexlet.code.app.model.DTO.TaskResponseDTO;
import hexlet.code.app.model.entity.TaskStatus;
import hexlet.code.app.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
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
    public ResponseEntity<TaskResponseDTO> getTaskById(@PathVariable String id) {
        TaskResponseDTO taskResponseDTO = taskService.getTaskById(id);
        return ResponseEntity.ok().body(taskResponseDTO);
    }

    @GetMapping(path = "")
    public ResponseEntity<List<TaskResponseDTO>> getAllTasks(@Param("taskStatus") TaskStatus taskStatus,
                                                             @Param("executorId") Long executorId,
                                                             @Param("labels") Long labels,
                                                             @Param("authorId") Long authorId) {
        List<TaskResponseDTO> taskResponseDTOS = taskService.getAllTasks(taskStatus, executorId, labels, authorId);
        return ResponseEntity.ok().body(taskResponseDTOS);
    }

    @PostMapping(path = "")
    public ResponseEntity<TaskResponseDTO> createTask(@RequestBody @Valid TaskRequestDTO taskRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(taskRequestDTO));
    }

    @PutMapping(path = TASK_ID)
    public ResponseEntity<TaskResponseDTO> updateTask(@PathVariable String id,
                                                      @RequestBody @Valid TaskRequestDTO taskRequestDTO) {
        TaskResponseDTO updatedTask = taskService.updateTask(id, taskRequestDTO);
        return ResponseEntity.ok().body(updatedTask);
    }

    @DeleteMapping(path = TASK_ID)
    public String deleteUser(@PathVariable String id) {
        return taskService.deleteTask(id);
    }

}
