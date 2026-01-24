
package org.example.mapper;

import org.example.dto.*;
import org.example.model.SprintBacklog;
import org.example.model.Task;
import org.example.model.UserStory;

import java.util.List;
import java.util.stream.Collectors;

public class SprintBacklogMapper {

	public SprintBacklogResponseDTO toSprintBacklogResponseDTO(SprintBacklog sprint) {
		if (sprint == null)
			return null;

		SprintBacklogResponseDTO dto = new SprintBacklogResponseDTO();
		dto.setId(sprint.getId());
		dto.setNom(sprint.getNom());
		dto.setDateDebut(sprint.getDateDebut());
		dto.setDateFin(sprint.getDateFin());

		if (sprint.getUserStories() != null && !sprint.getUserStories().isEmpty()) {

			// Mapping des user stories
			dto.setUserStories(
					sprint.getUserStories().stream()
							.map(this::toUserStoryListDTO)
							.collect(Collectors.toList()));

			// Récupération de toutes les tasks du sprint
			List<TaskListDTO> tasks = sprint.getUserStories().stream()
					.filter(us -> us.getTasks() != null)
					.flatMap(us -> us.getTasks().stream())
					.map(this::toTaskListDTO)
					.collect(Collectors.toList());

			dto.setTasks(tasks);
		}

		return dto;
	}

	private UserStoryListDto toUserStoryListDTO(UserStory userStory) {
		if (userStory == null)
			return null;

		UserStoryListDto dto = new UserStoryListDto();
		dto.setId(userStory.getId());
		dto.setTitre(userStory.getTitle());
		dto.setDescription(userStory.getDescription());
		dto.setStatus(userStory.getStatus());

		return dto;
	}

	private TaskListDTO toTaskListDTO(Task task) {
		if (task == null)
			return null;

		TaskListDTO dto = new TaskListDTO();
		dto.setId(task.getId());
		dto.setTitre(task.getTitle());

		if (task.getUserStory() != null) {
			dto.setUserStory(task.getUserStory());
		}

		return dto;
	}

	public SprintBacklogListDTO toSprintBacklogListDTO(SprintBacklog sprint) {
		if (sprint == null)
			return null;

		SprintBacklogListDTO dto = new SprintBacklogListDTO();
		dto.setId(sprint.getId());
		dto.setNom(sprint.getNom());
		dto.setDateDebut(sprint.getDateDebut());
		dto.setDateFin(sprint.getDateFin());

		return dto;
	}

	public SprintBacklog toSprintBacklogEntity(SprintBacklogCreateDTO dto) {
		if (dto == null)
			return null;

		SprintBacklog sprint = new SprintBacklog();
		sprint.setNom(dto.getNom());
		sprint.setDateDebut(dto.getDateDebut());
		sprint.setDateFin(dto.getDateFin());

		return sprint;
	}

	public void updateSprintBacklogFromDTO(SprintBacklog sprint, SprintBacklogUpdateDTO dto) {
		if (sprint == null || dto == null)
			return;

		if (dto.getNom() != null) {
			sprint.setNom(dto.getNom());
		}
		if (dto.getDateDebut() != null) {
			sprint.setDateDebut(dto.getDateDebut());
		}
		if (dto.getDateFin() != null) {
			sprint.setDateFin(dto.getDateFin());
		}
	}

	public org.example.model.UserStory convertToUserStory(AddUserStoryToSprintDTO dto) {
		org.example.model.UserStory userStory = new org.example.model.UserStory();
		userStory.setId(dto.getUserStoryId());
		userStory.setTitle(dto.getTitre());
		userStory.setDescription(dto.getRole(), dto.getAction(), dto.getGoal());
		userStory.setPriority(dto.getPriority());
		userStory.setStatus(dto.getStatus());
		return userStory;
	}

	public org.example.model.UserStory convertToUserStory(RemoveUserStoryDTO dto) {
		org.example.model.UserStory userStory = new org.example.model.UserStory();
		userStory.setId(dto.getUserStoryId());
		return userStory;
	}

	public org.example.model.Task convertToTask(AddTaskToSprintDTO dto) {
		org.example.model.Task task = new org.example.model.Task();
		task.setTitle(dto.getTitre());
		task.setDescription(dto.getDescription());
		return task;
	}

	public org.example.model.Task convertToTask(RemoveTaskDTO dto) {
		org.example.model.Task task = new org.example.model.Task();
		task.setId(dto.getTaskId());
		return task;
	}
}
