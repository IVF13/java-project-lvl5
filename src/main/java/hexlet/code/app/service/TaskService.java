package hexlet.code.app.service;

import com.querydsl.core.types.Predicate;
import hexlet.code.app.model.DTO.TaskRequestDTO;
import hexlet.code.app.model.DTO.TaskResponseDTO;

import java.util.List;

public interface TaskService {

    TaskResponseDTO getTaskById(String id);

    List<TaskResponseDTO> getAllTasks(Predicate predicate);

    TaskResponseDTO createTask(TaskRequestDTO taskRequestDTO);

    TaskResponseDTO updateTask(String id, TaskRequestDTO taskRequestDTO);

    String deleteTask(String id);

}
