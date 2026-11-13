package org.example.sprint;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import org.example.backlog.BacklogMapper;
import org.example.backlog.Priority;
import org.example.backlog.ProductBacklog;
import org.example.backlog.UserStory;
import org.example.backlog.UserStoryRepository;
import org.example.common.BadRequestException;
import org.example.project.Project;
import org.example.project.ProjectAccessService;
import org.example.project.ProjectService;
import org.example.sprint.dto.CreateSprintRequest;
import org.example.sprint.dto.CreateTaskRequest;
import org.example.sprint.dto.TaskResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SprintServiceImplTest {

    @Mock
    private SprintBacklogRepository sprintRepository;
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private UserStoryRepository userStoryRepository;
    @Mock
    private ProjectService projectService;
    @Mock
    private ProjectAccessService accessService;
    @Mock
    private SprintMapper sprintMapper;
    @Spy
    private BacklogMapper backlogMapper = new BacklogMapper();

    @InjectMocks
    private SprintServiceImpl service;

    private Project project(long id) {
        Project project = Project.builder().id(id).title("P").build();
        ProductBacklog backlog = ProductBacklog.builder().id(id * 10).title("B").project(project).build();
        project.setProductBacklog(backlog);
        return project;
    }

    private SprintBacklog sprint(Project project) {
        return SprintBacklog.builder()
                .id(100L)
                .name("Sprint 1")
                .startDate(LocalDate.of(2026, 1, 1))
                .endDate(LocalDate.of(2026, 1, 14))
                .project(project)
                .userStories(new ArrayList<>())
                .tasks(new ArrayList<>())
                .build();
    }

    @Test
    void create_rejectsEndDateBeforeStartDate() {
        assertThatThrownBy(() -> service.create(
                1L,
                new CreateSprintRequest("Sprint", LocalDate.of(2026, 2, 1), LocalDate.of(2026, 1, 1)),
                "alice"))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    void addUserStory_rejectsStoryFromAnotherProject() {
        Project project = project(1L);
        SprintBacklog sprint = sprint(project);
        UserStory story = UserStory.builder().id(50L).productBacklog(project(2L).getProductBacklog()).build();

        when(accessService.requireScrumRoleForSprint(100L, "alice")).thenReturn(null);
        when(accessService.getSprint(100L)).thenReturn(sprint);
        when(accessService.getStory(50L)).thenReturn(story);

        assertThatThrownBy(() -> service.addUserStory(100L, 50L, "alice"))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    void addUserStory_setsBothSides() {
        Project project = project(1L);
        SprintBacklog sprint = sprint(project);
        UserStory story = UserStory.builder().id(50L).productBacklog(project.getProductBacklog()).build();

        when(accessService.requireScrumRoleForSprint(100L, "alice")).thenReturn(null);
        when(accessService.getSprint(100L)).thenReturn(sprint);
        when(accessService.getStory(50L)).thenReturn(story);

        service.addUserStory(100L, 50L, "alice");

        assertThat(story.getSprintBacklog()).isSameAs(sprint);
        assertThat(sprint.getUserStories()).containsExactly(story);
    }

    @Test
    void addTask_rejectsStoryNotInSprint() {
        Project project = project(1L);
        SprintBacklog sprint = sprint(project);
        UserStory story = UserStory.builder().id(50L).productBacklog(project.getProductBacklog()).build();

        when(accessService.requireMemberForSprint(100L, "alice")).thenReturn(null);
        when(accessService.getSprint(100L)).thenReturn(sprint);
        when(accessService.getStory(50L)).thenReturn(story);

        assertThatThrownBy(() -> service.addTask(100L, new CreateTaskRequest(50L, "Task", "d"), "alice"))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    void addTask_setsStoryAndSprintAndSaves() {
        Project project = project(1L);
        SprintBacklog sprint = sprint(project);
        UserStory story = UserStory.builder()
                .id(50L)
                .title("Story")
                .priority(Priority.MUST_HAVE)
                .productBacklog(project.getProductBacklog())
                .tasks(new ArrayList<>())
                .build();
        sprint.addUserStory(story);

        when(accessService.requireMemberForSprint(100L, "alice")).thenReturn(null);
        when(accessService.getSprint(100L)).thenReturn(sprint);
        when(accessService.getStory(50L)).thenReturn(story);
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(sprintMapper.toResponse(any(Task.class)))
                .thenReturn(new TaskResponse(1L, "Task", "d", Status.TO_DO, 50L, 100L));

        TaskResponse response = service.addTask(100L, new CreateTaskRequest(50L, "Task", "d"), "alice");

        assertThat(response.title()).isEqualTo("Task");
        assertThat(story.getTasks()).hasSize(1);
        assertThat(sprint.getTasks()).hasSize(1);
        Task task = story.getTasks().get(0);
        assertThat(task.getUserStory()).isSameAs(story);
        assertThat(task.getSprintBacklog()).isSameAs(sprint);
        assertThat(task.getStatus()).isEqualTo(Status.TO_DO);
    }

    @Test
    void removeUserStory_clearsOwningSide() {
        Project project = project(1L);
        SprintBacklog sprint = sprint(project);
        UserStory story = UserStory.builder().id(50L).productBacklog(project.getProductBacklog()).build();
        sprint.addUserStory(story);

        when(accessService.requireScrumRoleForSprint(100L, "alice")).thenReturn(null);
        when(accessService.getSprint(100L)).thenReturn(sprint);
        when(accessService.getStory(50L)).thenReturn(story);

        service.removeUserStory(100L, 50L, "alice");

        assertThat(story.getSprintBacklog()).isNull();
        assertThat(sprint.getUserStories()).isEmpty();
    }
}
