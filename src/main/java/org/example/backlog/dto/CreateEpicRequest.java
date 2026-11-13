package org.example.backlog.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateEpicRequest(
        @NotBlank(message = "Epic title is required")
        @Size(max = 200, message = "Epic title must not exceed 200 characters")
        String title,

        @Size(max = 1000, message = "Description must not exceed 1000 characters")
        String description,

        @Size(max = 500, message = "Goal must not exceed 500 characters")
        String goal,

        LocalDate targetDate) {
}
