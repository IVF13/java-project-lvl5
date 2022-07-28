package hexlet.code.app.service.Impl;

import hexlet.code.app.model.entity.Task;
import hexlet.code.app.repository.TaskRepository;
import hexlet.code.app.service.TaskService;
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

    @Override
    public Task getTaskById(String id) {
        Task task = taskRepository.findById(Long.parseLong(id)).orElse(null);

        if (task == null) {
            throw new NotFoundException("Task Not Found");
        }

        return task;
    }

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    public Task createTask(Task task) {

        if (taskRepository.findByName(task.getName()).isPresent()
                && taskRepository.findByTaskStatusId(task.getTaskStatus().getId()).isPresent()) {
            throw new EntityExistsException("Task already exists");
        }

        taskRepository.save(task);
        task = taskRepository.findByName(task.getName()).get();

        return task;
    }

    @Override
    public Task updateTask(String id, Task task) {
        Task taskToUpdate = taskRepository.findById(Long.parseLong(id)).orElse(null);

        if (taskToUpdate == null) {
            throw new NotFoundException("Task Not Found");
        }

        taskToUpdate.setName(task.getName());

        return taskRepository.save(taskToUpdate);
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
