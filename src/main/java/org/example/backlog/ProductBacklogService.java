package org.example.backlog;

import java.util.List;

import org.example.backlog.dto.CreateUserStoryRequest;
import org.example.backlog.dto.ProductBacklogResponse;
import org.example.backlog.dto.UserStoryResponse;

public interface ProductBacklogService {

    ProductBacklogResponse getByProjectId(Long projectId, String username);

    ProductBacklogResponse getById(Long backlogId, String username);

    ProductBacklog getEntity(Long backlogId);

    ProductBacklog getEntityByProjectId(Long projectId);

    List<UserStoryResponse> getStories(Long backlogId, String username);

    UserStoryResponse addUserStory(Long backlogId, CreateUserStoryRequest request, String username);
}
