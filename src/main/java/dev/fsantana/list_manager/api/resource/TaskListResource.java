package dev.fsantana.list_manager.api.resource;


import dev.fsantana.list_manager.api.dto.TaskListDTO;
import dev.fsantana.list_manager.api.dto.input.InputTaskList;
import dev.fsantana.list_manager.config.docs.TaskListResourceDocs;
import dev.fsantana.list_manager.domain.model.TaskList;
import dev.fsantana.list_manager.domain.service.TaskListService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/task-lists")
@AllArgsConstructor
public class TaskListResource implements TaskListResourceDocs{

    private final TaskListService service;

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<TaskListDTO> getByID(@PathVariable Long id) {
        TaskList tasklist = service.findById(id);
        TaskListDTO dto = TaskListDTO.builder()
                .id(tasklist.getId())
                .title(tasklist.getTitle())
                .createdAt(tasklist.getCreatedAt())
                .build();
        return ResponseEntity.ok(dto);
    }

    @Override
    @GetMapping
    public ResponseEntity<Page<TaskListDTO>> findList(String title, Pageable pageable) {
        Page<TaskList> pageableList = service.findAll(title, pageable);
        List<TaskListDTO> list = pageableList.getContent().stream().map( taskList -> TaskListDTO.builder()
                .id(taskList.getId())
                .createdAt(taskList.getCreatedAt())
                .title(taskList.getTitle()).build()).toList();
        return ResponseEntity.ok(new PageImpl<>(list, pageableList.getPageable(), pageableList.getTotalElements() ));
    }

    @Override
    @PostMapping
    public ResponseEntity<TaskListDTO> create(@Valid @RequestBody InputTaskList input) {
        TaskList save = service.save(new TaskList(input.title()));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(save.getId())
                .toUri();
        TaskListDTO dto = TaskListDTO.builder().id(save.getId()).title(save.getTitle()).createdAt(save.getCreatedAt()).build();
        return ResponseEntity.created(location).body(dto);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<TaskListDTO> update(@PathVariable Long id,  @Valid @RequestBody InputTaskList input) {
        TaskList update = service.update(id, input.title());
        TaskListDTO dto = TaskListDTO.builder().id(update.getId()).title(update.getTitle()).createdAt(update.getCreatedAt()).build();
        return ResponseEntity.ok(dto);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        this.service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
