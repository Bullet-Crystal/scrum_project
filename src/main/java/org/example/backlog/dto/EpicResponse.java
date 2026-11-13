package org.example.backlog.dto;

import java.time.LocalDate;

public record EpicResponse(
        Long id,
        String title,
        String description,
        String goal,
        LocalDate creationDate,
        LocalDate targetDate,
        Long productBacklogId) {
}
