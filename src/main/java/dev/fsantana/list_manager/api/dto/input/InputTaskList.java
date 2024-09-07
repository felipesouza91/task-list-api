package dev.fsantana.list_manager.api.dto.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record InputTaskList(@NotBlank() @Size(max = 150) String title) {
}
