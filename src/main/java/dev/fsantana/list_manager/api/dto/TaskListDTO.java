package dev.fsantana.list_manager.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.OffsetDateTime;

@Builder
public record TaskListDTO(Long id, String title, OffsetDateTime createdAt) {
}
