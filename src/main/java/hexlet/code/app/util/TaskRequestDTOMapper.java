package hexlet.code.app.util;

import hexlet.code.app.model.DTO.TaskRequestDTO;
import hexlet.code.app.model.entity.Task;
import hexlet.code.app.repository.TaskStatusRepository;
import hexlet.code.app.repository.UserRepository;
import hexlet.code.app.service.UserService;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(imports = Task.class, componentModel = "spring")
public abstract class TaskRequestDTOMapper {

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    public abstract Task taskRequestDTOToTask(TaskRequestDTO taskRequestDTO);

    @AfterMapping
    void taskRequestDTOToTask(@MappingTarget Task task, TaskRequestDTO taskRequestDTO) {
        task.setTaskStatus(taskStatusRepository.getById(taskRequestDTO.getTaskStatusId()));
        task.setExecutor(userRepository.getById(taskRequestDTO.getExecutorId()));
        task.setAuthor(userRepository.findById(userService.getCurrentUser().getId()).get());
    }

}
