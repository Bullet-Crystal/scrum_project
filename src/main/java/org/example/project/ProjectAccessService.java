package org.example.project;

import java.util.Set;

import org.example.backlog.Epic;
import org.example.backlog.EpicRepository;
import org.example.backlog.ProductBacklog;
import org.example.backlog.ProductBacklogRepository;
import org.example.backlog.UserStory;
import org.example.backlog.UserStoryRepository;
import org.example.common.ForbiddenException;
import org.example.common.NotFoundException;
import org.example.sprint.SprintBacklog;
import org.example.sprint.SprintBacklogRepository;
import org.example.sprint.Task;
import org.example.sprint.TaskRepository;
import org.example.user.RoleType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectAccessService {

    public static final Set<RoleType> OWNER = Set.of(RoleType.PRODUCT_OWNER);
    public static final Set<RoleType> SCRUM_ROLES = Set.of(RoleType.PRODUCT_OWNER, RoleType.SCRUM_MASTER);
    public static final Set<RoleType> ALL_ROLES = Set.of(
            RoleType.PRODUCT_OWNER, RoleType.SCRUM_MASTER, RoleType.DEVELOPER);

    private final ProjectMemberRepository memberRepository;
    private final ProductBacklogRepository backlogRepository;
    private final EpicRepository epicRepository;
    private final UserStoryRepository userStoryRepository;
    private final SprintBacklogRepository sprintRepository;
    private final TaskRepository taskRepository;

    public ProjectMember requireMember(Long projectId, String username) {
        return memberRepository.findByProjectIdAndUserUsername(projectId, username)
                .orElseThrow(() -> new ForbiddenException("You are not a member of this project"));
    }

    public ProjectMember requireRole(Long projectId, String username, Set<RoleType> allowedRoles) {
        ProjectMember member = requireMember(projectId, username);
        if (!allowedRoles.contains(member.getRole())) {
            throw new ForbiddenException("You do not have permission to perform this action");
        }
        return member;
    }

    public ProjectMember requireOwner(Long projectId, String username) {
        return requireRole(projectId, username, OWNER);
    }

    public ProjectMember requireScrumRole(Long projectId, String username) {
        return requireRole(projectId, username, SCRUM_ROLES);
    }

    public ProjectMember requireMemberForBacklog(Long backlogId, String username) {
        return requireMember(projectIdOfBacklog(backlogId), username);
    }

    public ProjectMember requireOwnerForBacklog(Long backlogId, String username) {
        return requireOwner(projectIdOfBacklog(backlogId), username);
    }

    public ProjectMember requireMemberForEpic(Long epicId, String username) {
        return requireMember(projectIdOfEpic(epicId), username);
    }

    public ProjectMember requireOwnerForEpic(Long epicId, String username) {
        return requireOwner(projectIdOfEpic(epicId), username);
    }

    public ProjectMember requireMemberForStory(Long storyId, String username) {
        return requireMember(projectIdOfStory(storyId), username);
    }

    public ProjectMember requireOwnerForStory(Long storyId, String username) {
        return requireOwner(projectIdOfStory(storyId), username);
    }

    public ProjectMember requireMemberForSprint(Long sprintId, String username) {
        return requireMember(projectIdOfSprint(sprintId), username);
    }

    public ProjectMember requireScrumRoleForSprint(Long sprintId, String username) {
        return requireScrumRole(projectIdOfSprint(sprintId), username);
    }

    public ProjectMember requireMemberForTask(Long taskId, String username) {
        return requireMember(projectIdOfTask(taskId), username);
    }

    public ProjectMember requireDeveloperForTask(Long taskId, String username) {
        return requireRole(projectIdOfTask(taskId), username, ALL_ROLES);
    }

    public Long projectIdOfBacklog(Long backlogId) {
        return getBacklog(backlogId).getProject().getId();
    }

    public Long projectIdOfEpic(Long epicId) {
        return getEpic(epicId).getProductBacklog().getProject().getId();
    }

    public Long projectIdOfStory(Long storyId) {
        return getStory(storyId).getProductBacklog().getProject().getId();
    }

    public Long projectIdOfSprint(Long sprintId) {
        return getSprint(sprintId).getProject().getId();
    }

    public Long projectIdOfTask(Long taskId) {
        Task task = getTask(taskId);
        if (task.getUserStory() != null) {
            return task.getUserStory().getProductBacklog().getProject().getId();
        }
        if (task.getSprintBacklog() != null) {
            return task.getSprintBacklog().getProject().getId();
        }
        throw new ForbiddenException("Task is not attached to a project");
    }

    public ProductBacklog getBacklog(Long backlogId) {
        return backlogRepository.findById(backlogId)
                .orElseThrow(() -> new NotFoundException("Product backlog not found: " + backlogId));
    }

    public Epic getEpic(Long epicId) {
        return epicRepository.findById(epicId)
                .orElseThrow(() -> new NotFoundException("Epic not found: " + epicId));
    }

    public UserStory getStory(Long storyId) {
        return userStoryRepository.findById(storyId)
                .orElseThrow(() -> new NotFoundException("User story not found: " + storyId));
    }

    public SprintBacklog getSprint(Long sprintId) {
        return sprintRepository.findById(sprintId)
                .orElseThrow(() -> new NotFoundException("Sprint not found: " + sprintId));
    }

    public Task getTask(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Task not found: " + taskId));
    }
}
