package dev.fsantana.list_manager.domain.repository;

import dev.fsantana.list_manager.domain.model.TaskItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TaskItemRepository extends JpaRepository<TaskItem, Long>, JpaSpecificationExecutor<TaskItem> {

    Page<TaskItem> findByTaskListId(Long taskListId, Pageable page);

}
