package dev.fsantana.list_manager.domain.service;

import dev.fsantana.list_manager.api.dto.TaskListDTO;
import dev.fsantana.list_manager.domain.execption.AppEntityNotFound;
import dev.fsantana.list_manager.domain.model.TaskList;
import dev.fsantana.list_manager.domain.repository.TaskListRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TaskListService {

    private final TaskListRepository taskListRepository;

    public TaskList findById(Long id) {
        return this.taskListRepository.findById(id).orElseThrow(() -> new AppEntityNotFound("Lista de Tarefa não encontrada"));
    }
}
