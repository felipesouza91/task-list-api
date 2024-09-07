package dev.fsantana.list_manager.domain.repository;

import dev.fsantana.list_manager.domain.model.TaskList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskListRepository extends JpaRepository<TaskList, Long> {

    Page<TaskList> findByTitleContaining(String title, Pageable page);

    Optional<TaskList> findByTitleIgnoreCase(String title);
}
