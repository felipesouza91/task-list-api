package dev.fsantana.list_manager.domain.service;

import dev.fsantana.list_manager.domain.execption.AppEntityNotFound;
import dev.fsantana.list_manager.domain.execption.AppRuleException;
import dev.fsantana.list_manager.domain.model.TaskItem;
import dev.fsantana.list_manager.domain.model.TaskList;
import dev.fsantana.list_manager.domain.repository.TaskItemRepository;
import dev.fsantana.list_manager.domain.repository.TaskListRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TaskListItemService {

    private TaskListRepository taskListRepository;
    private TaskItemRepository taskItemRepository;

    public Page<TaskItem> find(Long taskListId, Pageable page) {
        this.findTaskListById(taskListId);
         return this.taskItemRepository.findByTaskListId(taskListId, page);
    }

    public TaskItem findById(Long taskListId, Long id) {
        this.findTaskListById(taskListId);
        return this.taskItemRepository.findById(id).orElseThrow(() -> new AppEntityNotFound("O item não existe ou não foi encontrado"));
    }

    public TaskItem save(Long taskListId, TaskItem taskItem) {
        TaskList taskList = findTaskListById(taskListId);
        taskItem.setTaskList(taskList);
        return this.taskItemRepository.save(taskItem);
    }

    public TaskItem update(Long taskListId, Long id, TaskItem dataInput) {
        TaskList taskListById = this.findTaskListById(taskListId);
        TaskItem savedTaskItem = this.taskItemRepository.findById(id).orElseThrow(() -> new AppEntityNotFound("O item não existe ou não foi encontrado"));
        if(!Objects.equals(savedTaskItem.getTaskList().getId(), taskListId)) {
            throw new AppRuleException("Não e possivel trocar a tarefa de lista");
        }
        BeanUtils.copyProperties(dataInput, savedTaskItem, "id", "createdAt", "taskList");
        return this.taskItemRepository.save(savedTaskItem);
    }

    public void delete(Long taskListId, Long id) {
        this.findTaskListById(taskListId);
        this.taskItemRepository.findById(id).orElseThrow(() -> new AppEntityNotFound("O item não existe ou não foi encontrado"));
        this.taskItemRepository.deleteById(id);
    }


    private TaskList findTaskListById(Long taskListId) {
        return this.taskListRepository.findById(taskListId)
                .orElseThrow(() -> new AppRuleException("Lista de tarefas não existes ou não foi encontrada"));
    }

}
