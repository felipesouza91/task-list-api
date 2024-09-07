package dev.fsantana.list_manager.api.dto.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.OffsetDateTime;

@Builder
public record InputTaskItem(
        @NotBlank  @Size(max = 160) String title,
        String description,
        Boolean isActive,
        Boolean isPriority
        ) {
}
