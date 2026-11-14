package org.example.sprint.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateSprintRequest(
        @NotBlank(message = "Sprint name is required")
        @Size(max = 100, message = "Sprint name must not exceed 100 characters")
        String name,

        @NotNull(message = "Start date is required") LocalDate startDate,

        @NotNull(message = "End date is required") LocalDate endDate) {
}
