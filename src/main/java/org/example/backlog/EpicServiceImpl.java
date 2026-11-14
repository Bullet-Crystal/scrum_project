package org.example.backlog;

import java.util.List;

import org.example.backlog.dto.CreateEpicRequest;
import org.example.backlog.dto.EpicResponse;
import org.example.backlog.dto.UpdateEpicRequest;
import org.example.backlog.dto.UserStoryResponse;
import org.example.common.BadRequestException;
import org.example.project.ProjectAccessService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class EpicServiceImpl implements EpicService {

    private final EpicRepository epicRepository;
    private final UserStoryRepository userStoryRepository;
    private final ProductBacklogService productBacklogService;
    private final ProjectAccessService accessService;
    private final BacklogMapper backlogMapper;

    @Override
    public EpicResponse create(Long backlogId, CreateEpicRequest request, String username) {
        accessService.requireOwnerForBacklog(backlogId, username);
        ProductBacklog backlog = productBacklogService.getEntity(backlogId);

        Epic epic = Epic.builder()
                .title(request.title())
                .description(request.description())
                .goal(request.goal())
                .targetDate(request.targetDate())
                .build();

        backlog.addEpic(epic);
        return backlogMapper.toResponse(epicRepository.save(epic));
    }

    @Override
    @Transactional(readOnly = true)
    public EpicResponse get(Long epicId, String username) {
        accessService.requireMemberForEpic(epicId, username);
        return backlogMapper.toResponse(accessService.getEpic(epicId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EpicResponse> getForBacklog(Long backlogId, String username) {
        accessService.requireMemberForBacklog(backlogId, username);
        return epicRepository.findByProductBacklogIdOrderByIdAsc(backlogId).stream()
                .map(backlogMapper::toResponse)
                .toList();
    }

    @Override
    public EpicResponse update(Long epicId, UpdateEpicRequest request, String username) {
        accessService.requireOwnerForEpic(epicId, username);
        Epic epic = accessService.getEpic(epicId);
        epic.setTitle(request.title());
        epic.setDescription(request.description());
        epic.setGoal(request.goal());
        epic.setTargetDate(request.targetDate());
        return backlogMapper.toResponse(epic);
    }

    @Override
    public void delete(Long epicId, String username) {
        accessService.requireOwnerForEpic(epicId, username);
        Epic epic = accessService.getEpic(epicId);

        // A story is not owned by an epic: detach it instead of deleting it.
        List.copyOf(epic.getUserStories()).forEach(epic::removeUserStory);
        epicRepository.delete(epic);
    }

    @Override
    public void addUserStory(Long epicId, Long userStoryId, String username) {
        accessService.requireOwnerForEpic(epicId, username);
        Epic epic = accessService.getEpic(epicId);
        UserStory story = accessService.getStory(userStoryId);

        if (!story.getProductBacklog().getId().equals(epic.getProductBacklog().getId())) {
            throw new BadRequestException("The user story belongs to a different product backlog");
        }
        if (story.getEpic() != null && !story.getEpic().getId().equals(epicId)) {
            throw new BadRequestException("The user story already belongs to another epic");
        }

        epic.addUserStory(story);
    }

    @Override
    public void removeUserStory(Long epicId, Long userStoryId, String username) {
        accessService.requireOwnerForEpic(epicId, username);
        Epic epic = accessService.getEpic(epicId);
        UserStory story = accessService.getStory(userStoryId);

        if (story.getEpic() != null && story.getEpic().getId().equals(epicId)) {
            epic.removeUserStory(story);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserStoryResponse> getStories(Long epicId, String username) {
        accessService.requireMemberForEpic(epicId, username);
        return userStoryRepository.findByEpicIdOrderByIdAsc(epicId).stream()
                .map(backlogMapper::toResponse)
                .toList();
    }
}
