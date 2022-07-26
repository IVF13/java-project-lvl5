package hexlet.code.app.service.Impl;

import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.repository.TaskStatusRepository;
import hexlet.code.app.service.TaskStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.webjars.NotFoundException;

import javax.persistence.EntityExistsException;
import java.util.List;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TaskStatusServiceImpl implements TaskStatusService {

    private final TaskStatusRepository taskStatusRepository;

    @Override
    public TaskStatus getTaskStatusById(String id) {

        TaskStatus taskStatus = taskStatusRepository.findById(Long.parseLong(id)).orElse(null);

        if (taskStatus == null) {
            throw new NotFoundException("User Not Found");
        }

        return taskStatus;
    }

    @Override
    public List<TaskStatus> getAllTaskStatuses() {
        return taskStatusRepository.findAll();
    }

    @Override
    public TaskStatus createTaskStatus(TaskStatus taskStatus) {
        if (taskStatusRepository.findByName(taskStatus.getName()).isPresent()) {
            throw new EntityExistsException("Status already exists");
        }

        taskStatusRepository.save(taskStatus);
        taskStatus = taskStatusRepository.findByName(taskStatus.getName()).get();

        return taskStatus;
    }

    @Override
    public TaskStatus updateTaskStatus(String id, TaskStatus taskStatus) {
        TaskStatus taskStatusToUpdate = taskStatusRepository.findById(Long.parseLong(id)).orElse(null);

        if (taskStatusToUpdate == null) {
            throw new NotFoundException("Task Status Not Found");
        }

        taskStatusToUpdate.setName(taskStatus.getName());

        return taskStatusRepository.save(taskStatusToUpdate);
    }

    @Override
    public String deleteTaskStatus(String id) {

        if (!taskStatusRepository.existsById(Long.parseLong(id))) {
            throw new NotFoundException("Task Status Not Found");
        } else {
            taskStatusRepository.deleteById(Long.parseLong(id));
        }

        return "Task status successfully deleted";
    }


}
