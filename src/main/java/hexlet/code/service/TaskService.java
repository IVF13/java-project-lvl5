package hexlet.code.service;

import com.querydsl.core.types.Predicate;
import hexlet.code.DTO.TaskRequestDTO;
import hexlet.code.DTO.TaskResponseDTO;

import java.util.List;

public interface TaskService {

    TaskResponseDTO getTaskById(Long id);

    List<TaskResponseDTO> getAllTasks(Predicate predicate);

    TaskResponseDTO createTask(TaskRequestDTO taskRequestDTO);

    TaskResponseDTO updateTask(Long id, TaskRequestDTO taskRequestDTO);

    void deleteTask(Long id);

}
