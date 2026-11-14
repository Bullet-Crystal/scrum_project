package org.example.sprint;

import org.example.project.ProjectAccessService;
import org.example.sprint.dto.TaskResponse;
import org.example.sprint.dto.UpdateTaskRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskServiceImpl implements TaskService {

    private final ProjectAccessService accessService;
    private final SprintMapper sprintMapper;

    @Override
    @Transactional(readOnly = true)
    public TaskResponse get(Long taskId, String username) {
        accessService.requireMemberForTask(taskId, username);
        return sprintMapper.toResponse(accessService.getTask(taskId));
    }

    @Override
    public TaskResponse update(Long taskId, UpdateTaskRequest request, String username) {
        accessService.requireDeveloperForTask(taskId, username);
        Task task = accessService.getTask(taskId);
        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setStatus(request.status());
        return sprintMapper.toResponse(task);
    }

    @Override
    public TaskResponse updateStatus(Long taskId, Status status, String username) {
        accessService.requireDeveloperForTask(taskId, username);
        Task task = accessService.getTask(taskId);
        task.setStatus(status);
        return sprintMapper.toResponse(task);
    }
}
