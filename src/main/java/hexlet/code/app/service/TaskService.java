package hexlet.code.app.service;

import hexlet.code.app.model.DTO.TaskDTO;
import hexlet.code.app.model.entity.Task;

import java.util.List;

public interface TaskService {

    Task getTaskById(String id);

    List<Task> getAllTasks();

    Task createTask(TaskDTO taskDTO);

    Task updateTask(String id, TaskDTO taskDTO);

    String deleteTask(String id);

}
