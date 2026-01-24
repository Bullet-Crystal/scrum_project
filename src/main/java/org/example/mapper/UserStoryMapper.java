package org.example.mapper;

import org.example.dto.UserStoryDto;
import org.example.dto.UserStoryResponseDto;
import org.example.model.Status;
import org.example.model.UserStory;
import org.springframework.stereotype.Component;

@Component
public class UserStoryMapper {

	public UserStoryResponseDto toResponse(UserStory userStory) {
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
				.status(userStory.getStatus())
				.creationDate(userStory.getCreationDate())
				.build();
	}

	public UserStory toEntity(UserStoryDto dto) {
		if (dto == null) {
			return null;
		}

		UserStory userStory = new UserStory();
		userStory.setTitle(dto.getTitle());
		userStory.setDescription(dto.getRole(), dto.getAction(), dto.getGoal());
		userStory.setPriority(dto.getPriority());
		userStory.setStatus(dto.getUserStoryStatut() != null ? dto.getUserStoryStatut() : Status.TO_DO);
		userStory.setCritereAcceptation(dto.getCritereAcceptation());

		return userStory;
	}

	public void updateEntityFromDto(UserStory userStory, UserStoryDto dto) {
		if (userStory == null || dto == null) {
			return;
		}

		if (dto.getTitle() != null) {
			userStory.setTitle(dto.getTitle());
		}
		if (dto.getRole() != null && dto.getAction() != null && dto.getGoal() != null) {
			userStory.setDescription(dto.getRole(), dto.getAction(), dto.getGoal());
		}
		if (dto.getPriority() != null) {
			userStory.setPriority(dto.getPriority());
		}
		if (dto.getUserStoryStatut() != null) {
			userStory.setStatus(dto.getUserStoryStatut());
		}
		if (dto.getCritereAcceptation() != null) {
			userStory.setCritereAcceptation(dto.getCritereAcceptation());
		}
	}
}
