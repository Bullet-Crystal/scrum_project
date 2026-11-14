package org.example.sprint.dto;

import org.example.sprint.Status;

public record TaskResponse(
        Long id,
        String title,
        String description,
        Status status,
        Long userStoryId,
        Long sprintBacklogId) {
}
