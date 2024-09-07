package dev.fsantana.list_manager.api.dto.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record InputUpdateTaskItem(
        @NotBlank  @Size(max = 160) String title,
        @NotBlank String description,
        @NotNull  Boolean isActive,
        @NotNull Boolean isPriority
        ) {
}
