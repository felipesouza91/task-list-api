package dev.fsantana.list_manager.api.resource;


import dev.fsantana.list_manager.api.dto.TaskListDTO;
import dev.fsantana.list_manager.domain.model.TaskList;
import dev.fsantana.list_manager.domain.repository.TaskListRepository;
import dev.fsantana.list_manager.domain.service.TaskListService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/task-lists")
@AllArgsConstructor
public class TaskListResource {

    private final TaskListService service;

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


}
