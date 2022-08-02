package hexlet.code.app.repository;


import hexlet.code.app.model.entity.QTask;
import hexlet.code.app.model.entity.Task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

import java.util.Optional;


public interface TaskRepository extends JpaRepository<Task, Long>, QuerydslPredicateExecutor<Task>,
        QuerydslBinderCustomizer<QTask> {
    Optional<Task> findByName(String name);

    Optional<Task> findByTaskStatusId(Long id);

    @Override
    default public void customize(QuerydslBindings bindings, QTask task) {
    }
}
