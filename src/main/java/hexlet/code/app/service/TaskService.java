package hexlet.code.app.service;

import hexlet.code.app.model.DTO.TaskRequestDTO;
import hexlet.code.app.model.DTO.TaskResponseDTO;

import java.util.List;

public interface TaskService {

    TaskResponseDTO getTaskById(String id);

    List<TaskResponseDTO> getAllTasks();

    TaskResponseDTO createTask(TaskRequestDTO taskRequestDTO);

    TaskResponseDTO updateTask(String id, TaskRequestDTO taskRequestDTO);

    String deleteTask(String id);

}
