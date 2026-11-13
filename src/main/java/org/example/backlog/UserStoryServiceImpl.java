package org.example.backlog;

import java.util.List;

import org.example.backlog.dto.UpdateUserStoryRequest;
import org.example.backlog.dto.UserStoryResponse;
import org.example.project.ProjectAccessService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserStoryServiceImpl implements UserStoryService {

    private final UserStoryRepository userStoryRepository;
    private final ProjectAccessService accessService;
    private final BacklogMapper backlogMapper;

    @Override
    @Transactional(readOnly = true)
    public UserStory getEntity(Long userStoryId) {
        return accessService.getStory(userStoryId);
    }

    @Override
    @Transactional(readOnly = true)
    public UserStoryResponse get(Long userStoryId, String username) {
        accessService.requireMemberForStory(userStoryId, username);
        return backlogMapper.toResponse(getEntity(userStoryId));
    }

    @Override
    public UserStoryResponse update(Long userStoryId, UpdateUserStoryRequest request, String username) {
        accessService.requireOwnerForStory(userStoryId, username);
        UserStory story = getEntity(userStoryId);

        story.setTitle(request.title());
        story.setDescription(request.role(), request.action(), request.goal());
        story.setPriority(request.priority());
        story.setStatus(request.status());
        story.setCritereAcceptation(request.acceptanceCriteria());

        return backlogMapper.toResponse(story);
    }

    @Override
    public void delete(Long userStoryId, String username) {
        accessService.requireOwnerForStory(userStoryId, username);
        UserStory story = getEntity(userStoryId);

        if (story.getEpic() != null) {
            story.getEpic().removeUserStory(story);
        }
        if (story.getSprintBacklog() != null) {
            story.getSprintBacklog().removeUserStory(story);
        }
        // Tasks are owned by the story, but the sprint also references them.
        // Detach them from the sprint before the cascade delete runs.
        List.copyOf(story.getTasks()).forEach(task -> {
            if (task.getSprintBacklog() != null) {
                task.getSprintBacklog().removeTask(task);
            }
        });
        userStoryRepository.delete(story);
    }

    @Override
    public UserStoryResponse prioritize(Long userStoryId, Priority priority, String username) {
        accessService.requireOwnerForStory(userStoryId, username);
        UserStory story = getEntity(userStoryId);
        story.setPriority(priority);
        return backlogMapper.toResponse(story);
    }
}
