package org.example.sprint.dto;

import jakarta.validation.constraints.NotNull;

public record AddUserStoryToSprintRequest(
        @NotNull(message = "User story id is required") Long userStoryId) {
}
