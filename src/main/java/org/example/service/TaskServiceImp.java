package org.example.service;

import org.example.repository.TaskRepository;
import org.example.model.Status;
import org.example.model.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskServiceImp implements TaskService {
	private final TaskRepository taskRepository;
	private final ProjectService projectService;

	@Override
	public void modifierTask(Task oldtask, Task newtask) {
		Task task2 = taskRepository.findById(oldtask.getId())
				.orElseThrow(() -> new IllegalArgumentException("Task not found"));
		task2.setId(newtask.getId());
		task2.setTitle(newtask.getTitle());
		task2.setDescription(newtask.getDescription());
		taskRepository.save(task2);
	}

	public void isAuthorizedInTask(Long id, String username) {
		Task task = getTaskById(id);
		projectService.isUserAuthorized(task.getUserStory().getProductBacklog().getProject().getId(), username);
	}

	@Override
	public Task getTaskById(Long id) {
		return taskRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Task not found"));
	}

	@Override
	public void modifierTaskStatus(Task task, Status status) {
		Task task1 = taskRepository.findById(task.getId())
				.orElseThrow(() -> new IllegalArgumentException("Task not found"));
		task1.setStatus(status);
		taskRepository.save(task1);

	}

}
