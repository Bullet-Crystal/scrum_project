
package org.example.mapper;

import org.example.dto.AddTaskToSprintDTO;
import org.example.dto.TaskCreateDTO;
import org.example.dto.TaskModificationDTO;
import org.example.dto.TaskResponseDTO;
import org.example.model.Status;
import org.example.model.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {
	public TaskResponseDTO toTaskResponseDTO(Task task) {
		if (task == null)
			return null;

		TaskResponseDTO dto = new TaskResponseDTO();
		dto.setId(task.getId());
		dto.setTitre(task.getTitle());
		dto.setDescription(task.getDescription());

		if (task.getUserStory() != null) {
			dto.setUserStory(task.getUserStory());
		}
		return dto;
	}

	public Task toTaskEntity(TaskCreateDTO dto) {
		if (dto == null)
			return null;

		Task task = new Task();
		task.setTitle(dto.getTitre());
		task.setDescription(dto.getDescription());
		task.setStatus(Status.TO_DO);

		return task;
	}

	public Task toTaskEntity(AddTaskToSprintDTO dto) {
		if (dto == null)
			return null;

		Task task = new Task();
		task.setTitle(dto.getTitre());
		task.setDescription(dto.getDescription());
		task.setStatus(dto.getStatus() != null ? dto.getStatus() : Status.TO_DO);

		return task;
	}

	public Task updateTaskFromDTO(Task task, TaskModificationDTO dto) {
		if (task == null || dto == null)

			if (dto.getTitre() != null) {
				task.setTitle(dto.getTitre());
			}
		if (dto.getDescription() != null) {
			task.setDescription(dto.getDescription());
		}
		if (dto.getStatus() != null) {
			task.setStatus(dto.getStatus());
		}
		return task;
	}
}
