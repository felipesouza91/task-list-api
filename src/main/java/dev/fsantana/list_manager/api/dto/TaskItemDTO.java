package dev.fsantana.list_manager.api.dto;

import lombok.Builder;

import java.time.OffsetDateTime;

@Builder
public record TaskItemDTO(
        Long id,
        String title,
        String description,
        Boolean isActive,
        Boolean isPriority,
        OffsetDateTime createdAt
        ) {
}
