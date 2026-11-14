package org.example.sprint;

import java.util.List;

import org.example.backlog.dto.UserStoryResponse;
import org.example.sprint.dto.CreateSprintRequest;
import org.example.sprint.dto.CreateTaskRequest;
import org.example.sprint.dto.SprintResponse;
import org.example.sprint.dto.TaskResponse;
import org.example.sprint.dto.UpdateSprintRequest;

public interface SprintService {

    SprintResponse create(Long projectId, CreateSprintRequest request, String username);

    SprintResponse get(Long sprintId, String username);

    List<SprintResponse> getForProject(Long projectId, String username);

    SprintResponse update(Long sprintId, UpdateSprintRequest request, String username);

    void delete(Long sprintId, String username);

    void addUserStory(Long sprintId, Long userStoryId, String username);

    void removeUserStory(Long sprintId, Long userStoryId, String username);

    List<UserStoryResponse> getStories(Long sprintId, String username);

    TaskResponse addTask(Long sprintId, CreateTaskRequest request, String username);

    void removeTask(Long sprintId, Long taskId, String username);

    List<TaskResponse> getTasks(Long sprintId, String username);
}
