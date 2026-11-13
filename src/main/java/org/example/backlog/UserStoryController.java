package org.example.backlog;

import org.example.backlog.dto.PrioritizeUserStoryRequest;
import org.example.backlog.dto.UpdateUserStoryRequest;
import org.example.backlog.dto.UserStoryResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/user-stories")
@RequiredArgsConstructor
public class UserStoryController {

    private final UserStoryService userStoryService;

    @GetMapping("/{userStoryId}")
    public UserStoryResponse getUserStory(
            @PathVariable Long userStoryId,
            Authentication authentication) {
        return userStoryService.get(userStoryId, authentication.getName());
    }

    @PutMapping("/{userStoryId}")
    public UserStoryResponse updateUserStory(
            @PathVariable Long userStoryId,
            @Valid @RequestBody UpdateUserStoryRequest request,
            Authentication authentication) {
        return userStoryService.update(userStoryId, request, authentication.getName());
    }

    @DeleteMapping("/{userStoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserStory(
            @PathVariable Long userStoryId,
            Authentication authentication) {
        userStoryService.delete(userStoryId, authentication.getName());
    }

    @PatchMapping("/{userStoryId}/priority")
    public UserStoryResponse prioritizeUserStory(
            @PathVariable Long userStoryId,
            @Valid @RequestBody PrioritizeUserStoryRequest request,
            Authentication authentication) {
        return userStoryService.prioritize(userStoryId, request.priority(), authentication.getName());
    }
}
