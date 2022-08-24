package hexlet.code.controller;


import hexlet.code.model.TaskStatus;
import hexlet.code.service.TaskStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

import javax.management.relation.RelationException;
import javax.validation.Valid;
import java.util.List;

import static hexlet.code.controller.TaskStatusController.TASK_STATUS_CONTROLLER_PATH;

@Validated
@RestController
@RequestMapping("${base-url}" + TASK_STATUS_CONTROLLER_PATH)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TaskStatusController {

    public static final String TASK_STATUS_CONTROLLER_PATH = "/statuses";

    public static final String TASK_STATUS_ID = "/{id}";

    private final TaskStatusService taskStatusService;

    @GetMapping(path = TASK_STATUS_ID)
    @ResponseStatus(HttpStatus.OK)
    public TaskStatus getTaskStatusById(@PathVariable Long id) {
        TaskStatus taskStatus = taskStatusService.getTaskStatusById(id);
        return taskStatus;
    }

    @GetMapping(path = "")
    @ResponseStatus(HttpStatus.OK)
    public List<TaskStatus> getAllTaskStatuses() {
        List<TaskStatus> taskStatuses = taskStatusService.getAllTaskStatuses();
        return taskStatuses;
    }

    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public TaskStatus createTaskStatus(@RequestBody @Valid TaskStatus taskStatus) {
        return taskStatusService.createTaskStatus(taskStatus);
    }

    @PutMapping(path = TASK_STATUS_ID)
    @ResponseStatus(HttpStatus.OK)
    public TaskStatus updateTaskStatus(@PathVariable Long id,
                                       @RequestBody @Valid TaskStatus taskStatus) {
        TaskStatus updatedTaskStatus = taskStatusService.updateTaskStatus(id, taskStatus);
        return updatedTaskStatus;
    }

    @DeleteMapping(path = TASK_STATUS_ID)
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@PathVariable Long id) throws RelationException {
        taskStatusService.deleteTaskStatus(id);
    }

}
