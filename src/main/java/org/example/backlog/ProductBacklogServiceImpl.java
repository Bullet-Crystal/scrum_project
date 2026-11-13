package org.example.backlog;

import java.util.List;

import org.example.backlog.dto.CreateUserStoryRequest;
import org.example.backlog.dto.ProductBacklogResponse;
import org.example.backlog.dto.UserStoryResponse;
import org.example.common.ConflictException;
import org.example.common.NotFoundException;
import org.example.project.ProjectAccessService;
import org.example.sprint.Status;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductBacklogServiceImpl implements ProductBacklogService {

    private final ProductBacklogRepository backlogRepository;
    private final UserStoryRepository userStoryRepository;
    private final ProjectAccessService accessService;
    private final BacklogMapper backlogMapper;

    @Override
    @Transactional(readOnly = true)
    public ProductBacklogResponse getByProjectId(Long projectId, String username) {
        accessService.requireMember(projectId, username);
        return backlogMapper.toResponse(getEntityByProjectId(projectId));
    }

    @Override
    @Transactional(readOnly = true)
    public ProductBacklogResponse getById(Long backlogId, String username) {
        accessService.requireMemberForBacklog(backlogId, username);
        return backlogMapper.toResponse(getEntity(backlogId));
    }

    @Override
    @Transactional(readOnly = true)
    public ProductBacklog getEntity(Long backlogId) {
        return backlogRepository.findById(backlogId)
                .orElseThrow(() -> new NotFoundException("Product backlog not found: " + backlogId));
    }

    @Override
    @Transactional(readOnly = true)
    public ProductBacklog getEntityByProjectId(Long projectId) {
        return backlogRepository.findByProjectId(projectId)
                .orElseThrow(() -> new NotFoundException("Product backlog not found for project: " + projectId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserStoryResponse> getStories(Long backlogId, String username) {
        accessService.requireMemberForBacklog(backlogId, username);
        return userStoryRepository.findByProductBacklogIdOrderByIdAsc(backlogId).stream()
                .map(backlogMapper::toResponse)
                .toList();
    }

    @Override
    public UserStoryResponse addUserStory(Long backlogId, CreateUserStoryRequest request, String username) {
        accessService.requireOwnerForBacklog(backlogId, username);
        ProductBacklog backlog = getEntity(backlogId);

        if (userStoryRepository.existsByProductBacklogIdAndTitleIgnoreCase(backlogId, request.title())) {
            throw new ConflictException("A user story with this title already exists in the backlog");
        }

        UserStory story = UserStory.builder()
                .title(request.title())
                .role(request.role())
                .action(request.action())
                .goal(request.goal())
                .priority(request.priority())
                .status(request.status() == null ? Status.TO_DO : request.status())
                .critereAcceptation(request.acceptanceCriteria())
                .build();

        backlog.addUserStory(story);
        return backlogMapper.toResponse(userStoryRepository.save(story));
    }
}
