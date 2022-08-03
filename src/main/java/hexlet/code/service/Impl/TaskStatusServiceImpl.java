package hexlet.code.service.Impl;

import hexlet.code.model.entity.Task;
import hexlet.code.model.entity.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.service.TaskStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import javax.management.relation.RelationException;
import javax.persistence.EntityExistsException;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TaskStatusServiceImpl implements TaskStatusService {

    private final TaskStatusRepository taskStatusRepository;

    @Override
    public TaskStatus getTaskStatusById(String id) {

        TaskStatus taskStatus = taskStatusRepository.findById(Long.parseLong(id)).orElse(null);

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
    public TaskStatus updateTaskStatus(String id, TaskStatus updatedTaskStatus) {
        TaskStatus existsTaskStatus = taskStatusRepository.findById(Long.parseLong(id)).orElse(null);

        if (existsTaskStatus == null) {
            throw new NotFoundException("Task Status Not Found");
        }
        updatedTaskStatus.setId(existsTaskStatus.getId());
        updatedTaskStatus.setCreatedAt(existsTaskStatus.getCreatedAt());

        taskStatusRepository.save(updatedTaskStatus);
        updatedTaskStatus = taskStatusRepository.findById(Long.parseLong(id)).get();

        return updatedTaskStatus;
    }

    @Override
    public String deleteTaskStatus(String id) throws RelationException {

        if (!taskStatusRepository.existsById(Long.parseLong(id))) {
            throw new NotFoundException("Task Status Not Found");
        } else {

            List<Task> tasks = taskStatusRepository.findById(Long.parseLong(id)).get().getTasks();

            if (!tasks.isEmpty()) {
                throw new RelationException("Task status have related tasks, unable to delete");
            }

            taskStatusRepository.deleteById(Long.parseLong(id));
        }

        return "Task status successfully deleted";
    }


}
