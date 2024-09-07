package dev.fsantana.list_manager.domain.service;

import dev.fsantana.list_manager.domain.execption.AppEntityNotFound;
import dev.fsantana.list_manager.domain.execption.AppRuleException;
import dev.fsantana.list_manager.domain.model.TaskList;
import dev.fsantana.list_manager.domain.repository.TaskListRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
@AllArgsConstructor
public class TaskListService {

    private final TaskListRepository taskListRepository;

    public TaskList findById(Long id) {
        return this.taskListRepository.findById(id).orElseThrow(() -> new AppEntityNotFound("Lista de tarefas não existes ou não foi encontrada"));
    }


    public Page<TaskList> findAll(String title, Pageable pageable) {
        if (!StringUtils.hasText(title)) {
            title = "";
        }
        return this.taskListRepository.findByTitleContaining(title, pageable);
    }

    public TaskList save(TaskList taskList) {
        Optional<TaskList> byTitleIgnoreCase = taskListRepository.findByTitleIgnoreCase(taskList.getTitle());
        if (byTitleIgnoreCase.isPresent()) {
            throw new AppRuleException("Já existe uma lista de tarefa de tarefas cadastradas");
        }
        return this.taskListRepository.save(taskList);
    }

    public TaskList update(Long id, String title) {
       TaskList savedTaskList =  this.taskListRepository.findById(id).orElseThrow(() -> new AppEntityNotFound("Lista de tarefas não existes ou não foi encontrada"));
        Optional<TaskList> byTitleIgnoreCase = taskListRepository.findByTitleIgnoreCase(title);
        if (byTitleIgnoreCase.isPresent()) {
            throw new AppRuleException("Já existe uma lista de tarefa de tarefas cadastradas");
        }
        savedTaskList.setTitle(title);
        return this.taskListRepository.save(savedTaskList);
    }

    public void delete(Long id) {
        this.taskListRepository.findById(id).orElseThrow(() -> new AppEntityNotFound("Lista de tarefas não existes ou não foi encontrada"));
        this.taskListRepository.deleteById(id);
    }
}
