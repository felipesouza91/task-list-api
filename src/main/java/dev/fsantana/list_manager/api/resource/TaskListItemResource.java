package dev.fsantana.list_manager.api.resource;


import dev.fsantana.list_manager.api.dto.TaskItemDTO;
import dev.fsantana.list_manager.api.dto.input.InputTaskItem;
import dev.fsantana.list_manager.domain.model.TaskItem;
import dev.fsantana.list_manager.domain.service.TaskListItemService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/task-lists/{taskListId}/items")
@AllArgsConstructor
public class TaskListItemResource {

    private TaskListItemService tasklistItemService;

    @GetMapping
    public ResponseEntity<Page<TaskItemDTO>> get(@PathVariable Long taskListId, Pageable page) {
        Page<TaskItem> result = this.tasklistItemService.find(taskListId, page);
        List<TaskItemDTO> dtoList = result.getContent().stream().map(item -> TaskItemDTO.builder()
                .id(item.getId())
                .title(item.getTitle())
                .description(item.getDescription())
                .isPriority(item.getIsPriority())
                .isActive(item.getIsActive())
                .createdAt(item.getCreatedAt())
                .build()
        ).toList();

        Page<TaskItemDTO> pageDto = new PageImpl<>(dtoList, page, result.getTotalElements());
        return ResponseEntity.ok(pageDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskItemDTO> get(@PathVariable Long taskListId, @PathVariable Long id) {
        TaskItem result = tasklistItemService.findById(taskListId, id);
        return ResponseEntity.ok(TaskItemDTO.builder()
                .id(result.getId())
                .title(result.getTitle())
                .description(result.getDescription())
                .isActive(result.getIsActive())
                .isPriority(result.getIsPriority())
                .createdAt(result.getCreatedAt())
                .build());
    }

    @PostMapping
    public ResponseEntity<TaskItemDTO> save(@PathVariable Long taskListId, @Valid @RequestBody InputTaskItem input) {
        TaskItem dataInput = new TaskItem(input.title(), input.description(), input.isActive(), input.isPriority());
        TaskItem result = tasklistItemService.save(taskListId, dataInput);
        Map<String, Long> expandMap = new HashMap<>();
        expandMap.put("taskListId", taskListId);
        expandMap.put("id", result.getId());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/task-lists/{taskListId}/items/{id}")
                .buildAndExpand(expandMap)
                .toUri();
        return ResponseEntity.created(location).body(
                TaskItemDTO.builder()
                        .id(result.getId())
                        .title(result.getTitle())
                        .description(result.getDescription())
                        .isPriority(result.getIsPriority())
                        .isActive(result.getIsActive())
                        .createdAt(result.getCreatedAt())
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskItemDTO> save(@PathVariable Long taskListId, @PathVariable Long id, @Valid @RequestBody InputTaskItem input) {
        TaskItem dataInput = new TaskItem(input.title(), input.description(), input.isActive(), input.isPriority());
        TaskItem result = tasklistItemService.update(taskListId, id, dataInput);
        return ResponseEntity.ok(
                TaskItemDTO.builder()
                        .id(result.getId())
                        .title(result.getTitle())
                        .description(result.getDescription())
                        .isPriority(result.getIsPriority())
                        .isActive(result.getIsActive())
                        .createdAt(result.getCreatedAt())
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long taskListId, @PathVariable Long id ) {
        this.tasklistItemService.delete(taskListId, id);
        return ResponseEntity.noContent().build();
    }
}