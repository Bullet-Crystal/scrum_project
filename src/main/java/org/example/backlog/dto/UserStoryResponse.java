package org.example.backlog.dto;

import java.time.LocalDate;

import org.example.backlog.Priority;
import org.example.sprint.Status;

public record UserStoryResponse(
        Long id,
        String title,
        String role,
        String action,
        String goal,
        String description,
        Priority priority,
        Status status,
        String acceptanceCriteria,
        LocalDate creationDate,
        Long epicId,
        Long productBacklogId,
        Long sprintBacklogId) {
}
