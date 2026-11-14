package org.example.backlog;

import org.example.backlog.dto.UpdateUserStoryRequest;
import org.example.backlog.dto.UserStoryResponse;

public interface UserStoryService {

    UserStory getEntity(Long userStoryId);

    UserStoryResponse get(Long userStoryId, String username);

    UserStoryResponse update(Long userStoryId, UpdateUserStoryRequest request, String username);

    void delete(Long userStoryId, String username);

    UserStoryResponse prioritize(Long userStoryId, Priority priority, String username);
}
