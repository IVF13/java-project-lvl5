package hexlet.code.service.Impl;

import com.querydsl.core.types.Predicate;
import hexlet.code.DTO.TaskRequestDTO;
import hexlet.code.DTO.TaskResponseDTO;
import hexlet.code.model.Task;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.service.TaskService;
import hexlet.code.mapper.TaskRequestDTOMapper;
import hexlet.code.mapper.TaskResponseDTOMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import javax.persistence.EntityExistsException;
import javax.transaction.Transactional;

import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    private final LabelRepository labelRepository;

    private final TaskRequestDTOMapper taskRequestDTOMapper;

    private final TaskResponseDTOMapper taskResponseDTOMapper;

    @Override
    public TaskResponseDTO getTaskById(Long id) {
        Task task = taskRepository.findById(id).orElse(null);

        if (task == null) {
            throw new NotFoundException("Task Not Found");
        }

        return taskResponseDTOMapper.taskToTaskResponseDTO(task);
    }

    @Override
    public List<TaskResponseDTO> getAllTasks(Predicate predicate) {
        List<Task> tasks = (List<Task>) taskRepository.findAll(predicate);
        return tasks.stream().map(taskResponseDTOMapper::taskToTaskResponseDTO).toList();
    }

    @Override
    public TaskResponseDTO createTask(TaskRequestDTO taskRequestDTO) {
        Task task = taskRequestDTOMapper.taskRequestDTOToTask(taskRequestDTO);

        if (taskRepository.findByName(task.getName()).isPresent()
                && taskRepository.findByTaskStatusId(task.getTaskStatus().getId()).isPresent()) {
            throw new EntityExistsException("Task already exists");
        }

        taskRepository.save(task);
        task = taskRepository.findByName(task.getName()).get();

        return taskResponseDTOMapper.taskToTaskResponseDTO(task);
    }

    @Override
    public TaskResponseDTO updateTask(Long id, TaskRequestDTO taskRequestDTO) {
        Task updatedTask = taskRequestDTOMapper.taskRequestDTOToTask(taskRequestDTO);
        Task existsTask = taskRepository.findById(id).orElse(null);

        if (existsTask == null) {
            throw new NotFoundException("Task Not Found");
        }
        updatedTask.setId(existsTask.getId());
        updatedTask.setCreatedAt(existsTask.getCreatedAt());

        taskRepository.save(updatedTask);
        updatedTask = taskRepository.findById(id).get();

        return taskResponseDTOMapper.taskToTaskResponseDTO(updatedTask);
    }

    @Override
    public String deleteTask(Long id) {

        if (!taskRepository.existsById(id)) {
            throw new NotFoundException("Task Not Found");
        } else {
            taskRepository.deleteById(id);
        }

        return "Task status successfully deleted";
    }

}
