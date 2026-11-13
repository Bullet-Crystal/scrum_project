package org.example.backlog;

import java.util.List;

import org.example.backlog.dto.EpicResponse;
import org.example.backlog.dto.UpdateEpicRequest;
import org.example.backlog.dto.UserStoryResponse;
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
@RequestMapping("/api/v1/epics")
@RequiredArgsConstructor
public class EpicController {

    private final EpicService epicService;

    @GetMapping("/{epicId}")
    public EpicResponse getEpic(@PathVariable Long epicId, Authentication authentication) {
        return epicService.get(epicId, authentication.getName());
    }

    @PutMapping("/{epicId}")
    public EpicResponse updateEpic(
            @PathVariable Long epicId,
            @Valid @RequestBody UpdateEpicRequest request,
            Authentication authentication) {
        return epicService.update(epicId, request, authentication.getName());
    }

    @DeleteMapping("/{epicId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEpic(@PathVariable Long epicId, Authentication authentication) {
        epicService.delete(epicId, authentication.getName());
    }

    @GetMapping("/{epicId}/user-stories")
    public List<UserStoryResponse> getStories(@PathVariable Long epicId, Authentication authentication) {
        return epicService.getStories(epicId, authentication.getName());
    }

    @PostMapping("/{epicId}/user-stories/{userStoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addUserStory(
            @PathVariable Long epicId,
            @PathVariable Long userStoryId,
            Authentication authentication) {
        epicService.addUserStory(epicId, userStoryId, authentication.getName());
    }

    @DeleteMapping("/{epicId}/user-stories/{userStoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeUserStory(
            @PathVariable Long epicId,
            @PathVariable Long userStoryId,
            Authentication authentication) {
        epicService.removeUserStory(epicId, userStoryId, authentication.getName());
    }
}
