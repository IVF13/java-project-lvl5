package hexlet.code.service;

import hexlet.code.model.TaskStatus;

import javax.management.relation.RelationException;
import java.util.List;

public interface TaskStatusService {

    TaskStatus getTaskStatusById(Long id);

    List<TaskStatus> getAllTaskStatuses();

    TaskStatus createTaskStatus(TaskStatus taskStatus);

    TaskStatus updateTaskStatus(Long id, TaskStatus taskStatus);

    String deleteTaskStatus(Long id) throws RelationException;

}
