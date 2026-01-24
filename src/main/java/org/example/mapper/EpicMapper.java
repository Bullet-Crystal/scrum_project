package org.example.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.example.dto.CreateEpicRequestDto;
import org.example.dto.EpicResponseDto;
import org.example.dto.UpdateEpicRequestDto;
import org.example.dto.UserStoryResponseDto;
import org.example.model.Epic;
import org.example.model.UserStory;
import org.springframework.stereotype.Component;

@Component
public class EpicMapper {

	public EpicResponseDto toEpicResponse(Epic epic) {
		if (epic == null) {
			return null;
		}

		List<UserStoryResponseDto> userStories = epic.getUserStories() != null
				? epic.getUserStories().stream()
						.map(this::toUserStoryResponse)
						.collect(Collectors.toList())
				: new ArrayList<>();

		return EpicResponseDto.builder()
				.id(epic.getId())
				.title(epic.getTitle())
				.description(epic.getDescription())
				.targetDate(epic.getTargetDate())
				.creationDate(epic.getCreationDate())
				.goal(epic.getGoal())
				.userStories(userStories)
				.build();
	}

	public UserStoryResponseDto toUserStoryResponse(UserStory userStory) {
		if (userStory == null) {
			return null;
		}

		return UserStoryResponseDto.builder()
				.id(userStory.getId())
				.title(userStory.getTitle())
				.role(userStory.getRole())
				.action(userStory.getAction())
				.goal(userStory.getGoal())
				.priority(userStory.getPriority())
				.build();
	}

	public Epic toEpic(CreateEpicRequestDto request) {
		if (request == null) {
			return null;
		}

		return Epic.builder()
				.title(request.getTitle())
				.description(request.getDescription())
				.targetDate(request.getTargetDate())
				.goal(request.getGoal())
				.build();
	}

	public void updateEpicFromRequest(Epic epic, UpdateEpicRequestDto request) {
		if (epic == null || request == null) {
			return;
		}

		if (request.getTitle() != null && !request.getTitle().isBlank()) {
			epic.setTitle(request.getTitle());
		}
	}
}
