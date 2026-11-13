package org.example.sprint;

import org.example.backlog.BacklogMapper;
import org.example.sprint.dto.SprintResponse;
import org.example.sprint.dto.TaskResponse;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SprintMapper {

    private final BacklogMapper backlogMapper;

    public SprintResponse toResponse(SprintBacklog sprint) {
        return new SprintResponse(
                sprint.getId(),
                sprint.getName(),
                sprint.getStartDate(),
                sprint.getEndDate(),
                sprint.getProject().getId(),
                sprint.getUserStories().stream().map(backlogMapper::toResponse).toList(),
                sprint.getTasks().stream().map(this::toResponse).toList());
    }

    public TaskResponse toResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getUserStory() == null ? null : task.getUserStory().getId(),
                task.getSprintBacklog() == null ? null : task.getSprintBacklog().getId());
    }
}
