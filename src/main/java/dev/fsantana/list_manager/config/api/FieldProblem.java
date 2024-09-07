package dev.fsantana.list_manager.config.api;

import lombok.Builder;

@Builder
public record FieldProblem(String name, String detail) {
}