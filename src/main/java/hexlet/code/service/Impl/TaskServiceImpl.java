package hexlet.code.service.Impl;

import com.querydsl.core.types.Predicate;
import hexlet.code.DTO.TaskRequestDTO;
import hexlet.code.DTO.TaskResponseDTO;
import hexlet.code.mapper.TaskRequestDTOMapper;
import hexlet.code.mapper.TaskResponseDTOMapper;
import hexlet.code.model.Task;
import hexlet.code.repository.TaskRepository;
import hexlet.code.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import javax.transaction.Transactional;
import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

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
        return taskResponseDTOMapper.taskToTaskResponseDTO(taskRepository.save(task));
    }

    @Override
    public TaskResponseDTO updateTask(Long id, TaskRequestDTO taskRequestDTO) {
        Task updatedTask = taskRequestDTOMapper.taskRequestDTOToTask(taskRequestDTO);
        Task taskToUpdate = taskRepository.findById(id).orElse(null);

        if (taskToUpdate == null) {
            throw new NotFoundException("Task Not Found");
        }

        taskToUpdate.setName(updatedTask.getName());
        taskToUpdate.setDescription(updatedTask.getDescription());
        taskToUpdate.setExecutor(updatedTask.getExecutor());
        taskToUpdate.setAuthor(updatedTask.getAuthor());
        taskToUpdate.setLabels(updatedTask.getLabels());
        taskToUpdate.setTaskStatus(updatedTask.getTaskStatus());

        return taskResponseDTOMapper.taskToTaskResponseDTO(taskRepository.save(taskToUpdate));
    }

    @Override
    public String deleteTask(Long id) {
        taskRepository.deleteById(id);
        return "Task status successfully deleted";
    }

}
