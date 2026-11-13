package org.example.sprint;

import org.example.sprint.dto.TaskResponse;
import org.example.sprint.dto.UpdateTaskRequest;

public interface TaskService {

    TaskResponse get(Long taskId, String username);

    TaskResponse update(Long taskId, UpdateTaskRequest request, String username);

    TaskResponse updateStatus(Long taskId, Status status, String username);
}
