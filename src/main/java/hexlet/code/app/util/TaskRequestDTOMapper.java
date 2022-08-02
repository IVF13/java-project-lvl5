package hexlet.code.app.util;

import hexlet.code.app.model.DTO.TaskRequestDTO;
import hexlet.code.app.model.entity.Task;
import hexlet.code.app.repository.LabelRepository;
import hexlet.code.app.repository.TaskStatusRepository;
import hexlet.code.app.repository.UserRepository;
import hexlet.code.app.service.UserService;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.webjars.NotFoundException;

import java.util.stream.Collectors;

@Mapper(imports = Task.class, componentModel = "spring")
public abstract class TaskRequestDTOMapper {

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private UserService userService;

    public abstract Task taskRequestDTOToTask(TaskRequestDTO taskRequestDTO);

    @AfterMapping
    void taskRequestDTOToTask(@MappingTarget Task task, TaskRequestDTO taskRequestDTO) {
        task.setTaskStatus(taskStatusRepository.getById(taskRequestDTO.getTaskStatusId()));

        if (taskRequestDTO.getExecutorId() != null) {
            task.setExecutor(userRepository.getById(taskRequestDTO.getExecutorId()));
        }

        if (taskRequestDTO.getLabelIds() != null) {
            task.setLabels(taskRequestDTO.getLabelIds().stream().map(x -> {
                        if (labelRepository.findById(x).isPresent()) {
                            return labelRepository.findById(x).get();
                        }
                        throw new NotFoundException("Label Not Found");
                    })
                    .collect(Collectors.toList()));
        }

        task.setAuthor(userRepository.findById(userService.getCurrentUser().getId()).get());
    }

}
