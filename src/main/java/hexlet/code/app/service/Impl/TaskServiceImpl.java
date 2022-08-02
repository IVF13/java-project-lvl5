package hexlet.code.app.service.Impl;

import hexlet.code.app.model.DTO.TaskRequestDTO;
import hexlet.code.app.model.DTO.TaskResponseDTO;
import hexlet.code.app.model.entity.Label;
import hexlet.code.app.model.entity.QTask;
import hexlet.code.app.model.entity.Task;
import hexlet.code.app.model.entity.TaskStatus;
import hexlet.code.app.repository.LabelRepository;
import hexlet.code.app.repository.TaskRepository;
import hexlet.code.app.service.TaskService;
import hexlet.code.app.util.TaskRequestDTOMapper;
import hexlet.code.app.util.TaskResponseDTOMapper;
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
    public TaskResponseDTO getTaskById(String id) {
        Task task = taskRepository.findById(Long.parseLong(id)).orElse(null);

        if (task == null) {
            throw new NotFoundException("Task Not Found");
        }

        return taskResponseDTOMapper.taskToTaskResponseDTO(task);
    }

    @Override
    public List<TaskResponseDTO> getAllTasks(TaskStatus taskStatus, Long executorId,
                                             Long labels, Long authorId) {
        Label existingLabel= new Label();

        if(labels != null) {
            existingLabel = labelRepository.findById(labels).get();
        }

        List<Task> tasks = (List<Task>) taskRepository.findAll(QTask.task.taskStatus.id.eq(taskStatus.getId())
                .and(QTask.task.executor.id.eq(executorId))
                .and(QTask.task.labels.any().eq(existingLabel))
                .and(QTask.task.author.id.eq(authorId)));
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
    public TaskResponseDTO updateTask(String id, TaskRequestDTO taskRequestDTO) {
        Task updatedTask = taskRequestDTOMapper.taskRequestDTOToTask(taskRequestDTO);
        Task existsTask = taskRepository.findById(Long.parseLong(id)).orElse(null);

        if (existsTask == null) {
            throw new NotFoundException("Task Not Found");
        }
        updatedTask.setId(existsTask.getId());
        updatedTask.setCreatedAt(existsTask.getCreatedAt());

        taskRepository.save(updatedTask);
        updatedTask = taskRepository.findById(Long.parseLong(id)).get();

        return taskResponseDTOMapper.taskToTaskResponseDTO(updatedTask);
    }

    @Override
    public String deleteTask(String id) {

        if (!taskRepository.existsById(Long.parseLong(id))) {
            throw new NotFoundException("Task Not Found");
        } else {
            taskRepository.deleteById(Long.parseLong(id));
        }

        return "Task status successfully deleted";
    }

}
