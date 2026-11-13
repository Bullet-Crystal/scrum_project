package org.example.backlog;

import org.example.backlog.dto.EpicResponse;
import org.example.backlog.dto.ProductBacklogResponse;
import org.example.backlog.dto.UserStoryResponse;
import org.springframework.stereotype.Component;

@Component
public class BacklogMapper {

    public ProductBacklogResponse toResponse(ProductBacklog backlog) {
        return new ProductBacklogResponse(
                backlog.getId(),
                backlog.getTitle(),
                backlog.getProject().getId());
    }

    public EpicResponse toResponse(Epic epic) {
        return new EpicResponse(
                epic.getId(),
                epic.getTitle(),
                epic.getDescription(),
                epic.getGoal(),
                epic.getCreationDate(),
                epic.getTargetDate(),
                epic.getProductBacklog().getId());
    }

    public UserStoryResponse toResponse(UserStory story) {
        return new UserStoryResponse(
                story.getId(),
                story.getTitle(),
                story.getRole(),
                story.getAction(),
                story.getGoal(),
                story.getDescription(),
                story.getPriority(),
                story.getStatus(),
                story.getCritereAcceptation(),
                story.getCreationDate(),
                story.getEpic() == null ? null : story.getEpic().getId(),
                story.getProductBacklog().getId(),
                story.getSprintBacklog() == null ? null : story.getSprintBacklog().getId());
    }
}
