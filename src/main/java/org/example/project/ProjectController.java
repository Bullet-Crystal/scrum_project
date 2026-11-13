package org.example.project;

import java.util.List;

import org.example.project.dto.AddProjectMemberRequest;
import org.example.project.dto.CreateProjectRequest;
import org.example.project.dto.ProjectMemberResponse;
import org.example.project.dto.ProjectResponse;
import org.example.project.dto.UpdateProjectRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping
    public List<ProjectResponse> getProjects(Authentication authentication) {
        return projectService.getForUser(authentication.getName());
    }

    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(
            @Valid @RequestBody CreateProjectRequest request,
            Authentication authentication) {
        ProjectResponse created = projectService.create(request, authentication.getName());
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(created.id())
                        .toUri())
                .body(created);
    }

    @GetMapping("/{projectId}")
    public ProjectResponse getProject(@PathVariable Long projectId, Authentication authentication) {
        return projectService.get(projectId, authentication.getName());
    }

    @PutMapping("/{projectId}")
    public ProjectResponse updateProject(
            @PathVariable Long projectId,
            @Valid @RequestBody UpdateProjectRequest request,
            Authentication authentication) {
        return projectService.update(projectId, request, authentication.getName());
    }

    @GetMapping("/{projectId}/members")
    public List<ProjectMemberResponse> getMembers(@PathVariable Long projectId, Authentication authentication) {
        return projectService.getMembers(projectId, authentication.getName());
    }

    @PostMapping("/{projectId}/members")
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectMemberResponse addMember(
            @PathVariable Long projectId,
            @Valid @RequestBody AddProjectMemberRequest request,
            Authentication authentication) {
        return projectService.addMember(projectId, request, authentication.getName());
    }

    @DeleteMapping("/{projectId}/members/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeMember(
            @PathVariable Long projectId,
            @PathVariable Long userId,
            Authentication authentication) {
        projectService.removeMember(projectId, userId, authentication.getName());
    }
}
