package org.example.sprint;

import java.util.List;

import org.example.backlog.BacklogMapper;
import org.example.backlog.UserStory;
import org.example.backlog.UserStoryRepository;
import org.example.backlog.dto.UserStoryResponse;
import org.example.common.BadRequestException;
import org.example.common.ConflictException;
import org.example.project.Project;
import org.example.project.ProjectAccessService;
import org.example.project.ProjectService;
import org.example.sprint.dto.CreateSprintRequest;
import org.example.sprint.dto.CreateTaskRequest;
import org.example.sprint.dto.SprintResponse;
import org.example.sprint.dto.TaskResponse;
import org.example.sprint.dto.UpdateSprintRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class SprintServiceImpl implements SprintService {

    private final SprintBacklogRepository sprintRepository;
    private final TaskRepository taskRepository;
    private final UserStoryRepository userStoryRepository;
    private final ProjectService projectService;
    private final ProjectAccessService accessService;
    private final SprintMapper sprintMapper;
    private final BacklogMapper backlogMapper;

    @Override
    public SprintResponse create(Long projectId, CreateSprintRequest request, String username) {
        accessService.requireScrumRole(projectId, username);
        validateDates(request.startDate(), request.endDate());

        if (sprintRepository.existsByProjectIdAndNameIgnoreCase(projectId, request.name())) {
            throw new ConflictException("A sprint with this name already exists in the project");
        }

        Project project = projectService.getEntity(projectId);
        SprintBacklog sprint = SprintBacklog.builder()
                .name(request.name())
                .startDate(request.startDate())
                .endDate(request.endDate())
                .project(project)
                .build();

        return sprintMapper.toResponse(sprintRepository.save(sprint));
    }

    @Override
    @Transactional(readOnly = true)
    public SprintResponse get(Long sprintId, String username) {
        accessService.requireMemberForSprint(sprintId, username);
        return sprintMapper.toResponse(accessService.getSprint(sprintId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SprintResponse> getForProject(Long projectId, String username) {
        accessService.requireMember(projectId, username);
        return sprintRepository.findByProjectIdOrderByStartDateAsc(projectId).stream()
                .map(sprintMapper::toResponse)
                .toList();
    }

    @Override
    public SprintResponse update(Long sprintId, UpdateSprintRequest request, String username) {
        accessService.requireScrumRoleForSprint(sprintId, username);
        validateDates(request.startDate(), request.endDate());

        SprintBacklog sprint = accessService.getSprint(sprintId);
        sprint.setName(request.name());
        sprint.setStartDate(request.startDate());
        sprint.setEndDate(request.endDate());
        return sprintMapper.toResponse(sprint);
    }

    @Override
    public void delete(Long sprintId, String username) {
        accessService.requireScrumRoleForSprint(sprintId, username);
        SprintBacklog sprint = accessService.getSprint(sprintId);

        // Sprints do not own stories or tasks: detach them before deleting the sprint.
        List.copyOf(sprint.getUserStories()).forEach(sprint::removeUserStory);
        List.copyOf(sprint.getTasks()).forEach(sprint::removeTask);
        sprintRepository.delete(sprint);
    }

    @Override
    public void addUserStory(Long sprintId, Long userStoryId, String username) {
        accessService.requireScrumRoleForSprint(sprintId, username);
        SprintBacklog sprint = accessService.getSprint(sprintId);
        UserStory story = accessService.getStory(userStoryId);

        if (!story.getProductBacklog().getProject().getId().equals(sprint.getProject().getId())) {
            throw new BadRequestException("The user story belongs to a different project");
        }
        if (story.getSprintBacklog() != null && !story.getSprintBacklog().getId().equals(sprintId)) {
            throw new ConflictException("The user story already belongs to another sprint");
        }

        sprint.addUserStory(story);
    }

    @Override
    public void removeUserStory(Long sprintId, Long userStoryId, String username) {
        accessService.requireScrumRoleForSprint(sprintId, username);
        SprintBacklog sprint = accessService.getSprint(sprintId);
        UserStory story = accessService.getStory(userStoryId);

        if (story.getSprintBacklog() != null && story.getSprintBacklog().getId().equals(sprintId)) {
            sprint.removeUserStory(story);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserStoryResponse> getStories(Long sprintId, String username) {
        accessService.requireMemberForSprint(sprintId, username);
        return userStoryRepository.findBySprintBacklogIdOrderByIdAsc(sprintId).stream()
                .map(backlogMapper::toResponse)
                .toList();
    }

    @Override
    public TaskResponse addTask(Long sprintId, CreateTaskRequest request, String username) {
        accessService.requireMemberForSprint(sprintId, username);
        SprintBacklog sprint = accessService.getSprint(sprintId);
        UserStory story = accessService.getStory(request.userStoryId());

        if (story.getSprintBacklog() == null || !story.getSprintBacklog().getId().equals(sprintId)) {
            throw new BadRequestException("Tasks can only be added to stories that are in this sprint");
        }

        Task task = Task.builder()
                .title(request.title())
                .description(request.description())
                .build();

        story.addTask(task);
        sprint.addTask(task);
        return sprintMapper.toResponse(taskRepository.save(task));
    }

    @Override
    public void removeTask(Long sprintId, Long taskId, String username) {
        accessService.requireMemberForSprint(sprintId, username);
        SprintBacklog sprint = accessService.getSprint(sprintId);
        Task task = accessService.getTask(taskId);

        if (task.getSprintBacklog() != null && task.getSprintBacklog().getId().equals(sprintId)) {
            sprint.removeTask(task);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponse> getTasks(Long sprintId, String username) {
        accessService.requireMemberForSprint(sprintId, username);
        return taskRepository.findBySprintBacklogIdOrderByIdAsc(sprintId).stream()
                .map(sprintMapper::toResponse)
                .toList();
    }

    private void validateDates(java.time.LocalDate startDate, java.time.LocalDate endDate) {
        if (endDate.isBefore(startDate)) {
            throw new BadRequestException("Sprint end date cannot be before its start date");
        }
    }
}
