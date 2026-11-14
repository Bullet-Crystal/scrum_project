package org.example.project.dto;

import java.time.LocalDate;

import org.example.backlog.dto.ProductBacklogResponse;

public record ProjectResponse(
        Long id,
        String title,
        String description,
        LocalDate creationDate,
        ProductBacklogResponse productBacklog) {
}
