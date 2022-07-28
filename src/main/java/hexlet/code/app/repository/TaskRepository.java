package hexlet.code.app.repository;

import hexlet.code.app.model.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findByName(String name);
    Optional<Task> findByTaskStatusId(Long id);
}
