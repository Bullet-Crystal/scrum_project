package org.example.backlog;

import java.util.List;

import org.example.backlog.dto.CreateEpicRequest;
import org.example.backlog.dto.CreateUserStoryRequest;
import org.example.backlog.dto.EpicResponse;
import org.example.backlog.dto.ProductBacklogResponse;
import org.example.backlog.dto.UserStoryResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ProductBacklogController {

    private final ProductBacklogService productBacklogService;
    private final EpicService epicService;

    @GetMapping("/projects/{projectId}/backlog")
    public ProductBacklogResponse getBacklogForProject(
            @PathVariable Long projectId,
            Authentication authentication) {
        return productBacklogService.getByProjectId(projectId, authentication.getName());
    }

    @GetMapping("/product-backlogs/{backlogId}")
    public ProductBacklogResponse getBacklog(
            @PathVariable Long backlogId,
            Authentication authentication) {
        return productBacklogService.getById(backlogId, authentication.getName());
    }

    @GetMapping("/product-backlogs/{backlogId}/user-stories")
    public List<UserStoryResponse> getStories(
            @PathVariable Long backlogId,
            Authentication authentication) {
        return productBacklogService.getStories(backlogId, authentication.getName());
    }

    @PostMapping("/product-backlogs/{backlogId}/user-stories")
    @ResponseStatus(HttpStatus.CREATED)
    public UserStoryResponse addUserStory(
            @PathVariable Long backlogId,
            @Valid @RequestBody CreateUserStoryRequest request,
            Authentication authentication) {
        return productBacklogService.addUserStory(backlogId, request, authentication.getName());
    }

    @GetMapping("/product-backlogs/{backlogId}/epics")
    public List<EpicResponse> getEpics(
            @PathVariable Long backlogId,
            Authentication authentication) {
        return epicService.getForBacklog(backlogId, authentication.getName());
    }

    @PostMapping("/product-backlogs/{backlogId}/epics")
    @ResponseStatus(HttpStatus.CREATED)
    public EpicResponse createEpic(
            @PathVariable Long backlogId,
            @Valid @RequestBody CreateEpicRequest request,
            Authentication authentication) {
        return epicService.create(backlogId, request, authentication.getName());
    }
}
