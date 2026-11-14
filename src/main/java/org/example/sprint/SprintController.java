package org.example.sprint;

import java.util.List;

import org.example.backlog.dto.UserStoryResponse;
import org.example.sprint.dto.AddUserStoryToSprintRequest;
import org.example.sprint.dto.CreateSprintRequest;
import org.example.sprint.dto.CreateTaskRequest;
import org.example.sprint.dto.SprintResponse;
import org.example.sprint.dto.TaskResponse;
import org.example.sprint.dto.UpdateSprintRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class SprintController {

    private final SprintService sprintService;

    @GetMapping("/projects/{projectId}/sprints")
    public List<SprintResponse> getSprints(
            @PathVariable Long projectId,
            Authentication authentication) {
        return sprintService.getForProject(projectId, authentication.getName());
    }

    @PostMapping("/projects/{projectId}/sprints")
    @ResponseStatus(HttpStatus.CREATED)
    public SprintResponse createSprint(
            @PathVariable Long projectId,
            @Valid @RequestBody CreateSprintRequest request,
            Authentication authentication) {
        return sprintService.create(projectId, request, authentication.getName());
    }

    @GetMapping("/sprints/{sprintId}")
    public SprintResponse getSprint(@PathVariable Long sprintId, Authentication authentication) {
        return sprintService.get(sprintId, authentication.getName());
    }

    @PutMapping("/sprints/{sprintId}")
    public SprintResponse updateSprint(
            @PathVariable Long sprintId,
            @Valid @RequestBody UpdateSprintRequest request,
            Authentication authentication) {
        return sprintService.update(sprintId, request, authentication.getName());
    }

    @DeleteMapping("/sprints/{sprintId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSprint(@PathVariable Long sprintId, Authentication authentication) {
        sprintService.delete(sprintId, authentication.getName());
    }

    @GetMapping("/sprints/{sprintId}/user-stories")
    public List<UserStoryResponse> getStories(
            @PathVariable Long sprintId,
            Authentication authentication) {
        return sprintService.getStories(sprintId, authentication.getName());
    }

    @PostMapping("/sprints/{sprintId}/user-stories")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addUserStory(
            @PathVariable Long sprintId,
            @Valid @RequestBody AddUserStoryToSprintRequest request,
            Authentication authentication) {
        sprintService.addUserStory(sprintId, request.userStoryId(), authentication.getName());
    }

    @DeleteMapping("/sprints/{sprintId}/user-stories/{userStoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeUserStory(
            @PathVariable Long sprintId,
            @PathVariable Long userStoryId,
            Authentication authentication) {
        sprintService.removeUserStory(sprintId, userStoryId, authentication.getName());
    }

    @GetMapping("/sprints/{sprintId}/tasks")
    public List<TaskResponse> getTasks(
            @PathVariable Long sprintId,
            Authentication authentication) {
        return sprintService.getTasks(sprintId, authentication.getName());
    }

    @PostMapping("/sprints/{sprintId}/tasks")
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponse addTask(
            @PathVariable Long sprintId,
            @Valid @RequestBody CreateTaskRequest request,
            Authentication authentication) {
        return sprintService.addTask(sprintId, request, authentication.getName());
    }

    @DeleteMapping("/sprints/{sprintId}/tasks/{taskId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeTask(
            @PathVariable Long sprintId,
            @PathVariable Long taskId,
            Authentication authentication) {
        sprintService.removeTask(sprintId, taskId, authentication.getName());
    }
}
