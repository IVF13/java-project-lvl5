package hexlet.code.app.service;

import hexlet.code.app.model.DTO.TaskRequestDTO;
import hexlet.code.app.model.DTO.TaskResponseDTO;
import hexlet.code.app.model.entity.TaskStatus;

import java.util.List;


public interface TaskService {

    TaskResponseDTO getTaskById(String id);

    List<TaskResponseDTO> getAllTasks(TaskStatus taskStatus, Long executorId, Long labels, Long authorId);

    TaskResponseDTO createTask(TaskRequestDTO taskRequestDTO);

    TaskResponseDTO updateTask(String id, TaskRequestDTO taskRequestDTO);

    String deleteTask(String id);

}
