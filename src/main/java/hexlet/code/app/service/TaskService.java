package hexlet.code.app.service;

import hexlet.code.app.model.entity.Task;

import java.util.List;

public interface TaskService {

    Task getTaskById(String id);

    List<Task> getAllTasks();

    Task createTask(Task task);

    Task updateTask(String id, Task task);

    String deleteTask(String id);

}
