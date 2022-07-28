package hexlet.code.app.util;

import hexlet.code.app.model.DTO.TaskDTO;
import hexlet.code.app.model.entity.Task;
import hexlet.code.app.repository.TaskStatusRepository;
import hexlet.code.app.repository.UserRepository;
import hexlet.code.app.service.UserService;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(imports = Task.class, componentModel = "spring")
public abstract class TaskDTOMapper {

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    public abstract Task taskDTOToTask(TaskDTO taskDTO);

    @AfterMapping
    void taskDTOToTaskAddTask(@MappingTarget Task task, TaskDTO taskDTO) {
        task.setTaskStatus(taskStatusRepository.getById(taskDTO.getTaskStatusId()));
        task.setExecutor(userRepository.getById(taskDTO.getExecutorId()));
        task.setAuthor(userRepository.findById(userService.getCurrentUser().getId()).get());
    }

}
