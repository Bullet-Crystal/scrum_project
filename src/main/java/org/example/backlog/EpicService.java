package org.example.backlog;

import java.util.List;

import org.example.backlog.dto.CreateEpicRequest;
import org.example.backlog.dto.EpicResponse;
import org.example.backlog.dto.UpdateEpicRequest;
import org.example.backlog.dto.UserStoryResponse;

public interface EpicService {

    EpicResponse create(Long backlogId, CreateEpicRequest request, String username);

    EpicResponse get(Long epicId, String username);

    List<EpicResponse> getForBacklog(Long backlogId, String username);

    EpicResponse update(Long epicId, UpdateEpicRequest request, String username);

    void delete(Long epicId, String username);

    void addUserStory(Long epicId, Long userStoryId, String username);

    void removeUserStory(Long epicId, Long userStoryId, String username);

    List<UserStoryResponse> getStories(Long epicId, String username);
}
