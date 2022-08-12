package hexlet.code.service.Impl;

import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.service.TaskStatusService;
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
public class TaskStatusServiceImpl implements TaskStatusService {

    private final TaskStatusRepository taskStatusRepository;

    @Override
    public TaskStatus getTaskStatusById(Long id) {

        TaskStatus taskStatus = taskStatusRepository.findById(id).orElse(null);

        if (taskStatus == null) {
            throw new NotFoundException("Task Status Not Found");
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
    public TaskStatus updateTaskStatus(Long id, TaskStatus updatedTaskStatus) {
        TaskStatus taskStatusToUpdate = taskStatusRepository.findById(id).orElse(null);

        if (taskStatusToUpdate == null) {
            throw new NotFoundException("Task Status Not Found");
        }

        taskStatusToUpdate.setName(updatedTaskStatus.getName());

        return taskStatusRepository.save(taskStatusToUpdate);
    }

    @Override
    public String deleteTaskStatus(Long id) {
        taskStatusRepository.deleteById(id);
        return "Task status successfully deleted";
    }


}
