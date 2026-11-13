package org.example.backlog.dto;

import org.example.backlog.Priority;

import jakarta.validation.constraints.NotNull;

public record PrioritizeUserStoryRequest(
        @NotNull(message = "Priority is required") Priority priority) {
}
