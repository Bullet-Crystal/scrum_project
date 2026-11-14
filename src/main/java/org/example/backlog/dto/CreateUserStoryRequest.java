package org.example.backlog.dto;

import org.example.backlog.Priority;
import org.example.sprint.Status;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateUserStoryRequest(
        @NotBlank(message = "User story title is required")
        @Size(max = 200, message = "Title must not exceed 200 characters")
        String title,

        @NotBlank(message = "Role is required")
        @Size(max = 100, message = "Role must not exceed 100 characters")
        String role,

        @NotBlank(message = "Action is required")
        @Size(max = 500, message = "Action must not exceed 500 characters")
        String action,

        @NotBlank(message = "Goal is required")
        @Size(max = 500, message = "Goal must not exceed 500 characters")
        String goal,

        @NotNull(message = "Priority is required")
        Priority priority,

        Status status,

        @Size(max = 500, message = "Acceptance criteria must not exceed 500 characters")
        String acceptanceCriteria) {
}
