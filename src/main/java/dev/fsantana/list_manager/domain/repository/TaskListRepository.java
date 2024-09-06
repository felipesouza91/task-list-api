package dev.fsantana.list_manager.domain.repository;

import dev.fsantana.list_manager.domain.model.TaskList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskListRepository extends JpaRepository<TaskList, Long> {
}
