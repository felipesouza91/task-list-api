package dev.fsantana.list_manager.config.docs;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import dev.fsantana.list_manager.api.dto.TaskListDTO;
import dev.fsantana.list_manager.api.dto.input.InputTaskList;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;


@Tag(name = "Task List")
public interface TaskListResourceDocs {
 

    public ResponseEntity<TaskListDTO> getByID( Long id);
    
    public ResponseEntity<Page<TaskListDTO>> findList(
        @Parameter(required = false, allowEmptyValue = true) String title,
        @Parameter(required = false, allowEmptyValue = true) Pageable pageable) ;

    public ResponseEntity<TaskListDTO> create(InputTaskList input);

    public ResponseEntity<TaskListDTO> update(Long id,  InputTaskList input) ;

    
    public ResponseEntity<?> delete( Long id);
}
