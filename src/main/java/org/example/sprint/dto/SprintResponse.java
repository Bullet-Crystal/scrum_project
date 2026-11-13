package org.example.sprint.dto;

import java.time.LocalDate;
import java.util.List;

import org.example.backlog.dto.UserStoryResponse;

public record SprintResponse(
        Long id,
        String name,
        LocalDate startDate,
        LocalDate endDate,
        Long projectId,
        List<UserStoryResponse> userStories,
        List<TaskResponse> tasks) {
}
