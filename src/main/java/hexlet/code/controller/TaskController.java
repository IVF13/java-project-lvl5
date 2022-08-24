package hexlet.code.controller;

import com.querydsl.core.types.Predicate;
import hexlet.code.DTO.TaskRequestDTO;
import hexlet.code.DTO.TaskResponseDTO;
import hexlet.code.model.Task;
import hexlet.code.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;


import static hexlet.code.controller.TaskController.TASK_CONTROLLER_PATH;

@Validated
@RestController
@RequestMapping("${base-url}" + TASK_CONTROLLER_PATH)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TaskController {

    public static final String TASK_CONTROLLER_PATH = "/tasks";

    public static final String TASK_ID = "/{id}";

    private final TaskService taskService;

    @Operation(summary = "Get task by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task found",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Task with that id not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping(path = TASK_ID)
    @ResponseStatus(HttpStatus.OK)
    public TaskResponseDTO getTaskById(@Parameter(description = "Id of task to be found")
                                                       @PathVariable Long id) {
        TaskResponseDTO taskResponseDTO = taskService.getTaskById(id);
        return taskResponseDTO;
    }

    @Operation(summary = "Get list of all tasks with filtering by params if required")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of all tasks",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping(path = "")
    @ResponseStatus(HttpStatus.OK)
    public List<TaskResponseDTO> getAllTasks(
            @Parameter(description = "Predicate that consumes the given parameters: "
                    + "taskStatus, executorId, labels, authorId")
            @QuerydslPredicate(root = Task.class)
            Predicate predicate) {
        List<TaskResponseDTO> taskResponseDTOS = taskService.getAllTasks(predicate);
        return taskResponseDTOS;
    }

    @Operation(summary = "Create new task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task created",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Task already exists"),
            @ApiResponse(responseCode = "422", description = "Not valid task data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponseDTO createTask(@Parameter(description = "Task data to save")
                                                      @RequestBody @Valid TaskRequestDTO taskRequestDTO) {
        return taskService.createTask(taskRequestDTO);
    }

    @Operation(summary = "Update task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task updated",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Task with that id not found"),
            @ApiResponse(responseCode = "422", description = "Not valid task data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PutMapping(path = TASK_ID)
    @ResponseStatus(HttpStatus.OK)
    public TaskResponseDTO updateTask(@Parameter(description = "Id of task to be updated")
                                                      @PathVariable Long id,
                                                      @Parameter(description = "Task data to update")
                                                      @RequestBody @Valid TaskRequestDTO taskRequestDTO) {
        TaskResponseDTO updatedTask = taskService.updateTask(id, taskRequestDTO);
        return updatedTask;
    }

    @Operation(summary = "Delete task by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task deleted",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Task with that id not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @DeleteMapping(path = TASK_ID)
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@Parameter(description = "Id of task to be deleted")
                             @PathVariable Long id) {
        taskService.deleteTask(id);
    }

}
