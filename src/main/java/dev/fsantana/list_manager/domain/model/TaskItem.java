package dev.fsantana.list_manager.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.Objects;

@Entity
@Table(name = "task_item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TaskItem {


    public TaskItem(String title, String description, Boolean isActive, Boolean isPriority) {
        this.title = title;
        this.description = description;
        this.isActive = Objects.requireNonNullElse(isActive, true);
        this.isPriority = Objects.requireNonNullElse(isPriority, false);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(unique = true, nullable = false)
    private String title;

    private String description;
    private Boolean isActive;
    private Boolean isPriority;

    @Column(name = "created_at")
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @ManyToOne
    @JoinColumn(name = "task_list_id", referencedColumnName = "id")
    private TaskList taskList;
}
