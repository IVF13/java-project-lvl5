package hexlet.code.mapper;

import hexlet.code.DTO.TaskRequestDTO;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.service.UserService;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        task.setAuthor(userService.getCurrentUser());

        if (taskRequestDTO.getExecutorId() != null) {
            task.setExecutor(Optional.ofNullable(taskRequestDTO.getExecutorId()).map(User::new).orElse(null));
        }

        task.setTaskStatus(Optional.ofNullable(taskRequestDTO.getTaskStatusId())
                .map(TaskStatus::new).orElse(null));

        if (taskRequestDTO.getLabelIds() != null) {
            List<Label> labels = new ArrayList<>();
            for (Long labelId : taskRequestDTO.getLabelIds()) {
                labels.add(Optional.ofNullable(labelId).map(Label::new).orElse(null));
            }
            task.setLabels(labels);
        }

    }

}
