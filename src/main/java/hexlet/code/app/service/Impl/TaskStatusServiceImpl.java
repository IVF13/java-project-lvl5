package hexlet.code.app.service.Impl;

import hexlet.code.app.model.entity.Task;
import hexlet.code.app.model.entity.TaskStatus;
import hexlet.code.app.repository.TaskStatusRepository;
import hexlet.code.app.service.TaskStatusService;
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
            throw new NotFoundException("Task status Not Found");
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
    public String deleteTaskStatus(String id) throws RelationException {

        if (!taskStatusRepository.existsById(Long.parseLong(id))) {
            throw new NotFoundException("Task Status Not Found");
        } else {

            List<Task> tasks = taskStatusRepository.findById(Long.parseLong(id)).get().getTasks();

            if (!tasks.isEmpty()) {
                throw new RelationException("Task status have assigned tasks, unable to delete");
            }

            taskStatusRepository.deleteById(Long.parseLong(id));
        }

        return "Task status successfully deleted";
    }


}
