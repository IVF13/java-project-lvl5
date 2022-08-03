package hexlet.code.service;

import hexlet.code.model.entity.TaskStatus;

import javax.management.relation.RelationException;
import java.util.List;

public interface TaskStatusService {

    TaskStatus getTaskStatusById(String id);

    List<TaskStatus> getAllTaskStatuses();

    TaskStatus createTaskStatus(TaskStatus taskStatus);

    TaskStatus updateTaskStatus(String id, TaskStatus taskStatus);

    String deleteTaskStatus(String id) throws RelationException;

}
