package org.example.dto;

import org.example.model.Priority;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUserStoryRequestDto {
	@Size(max = 200, message = "Title must not exceed 200 characters")
	private String title;

	@Size(max = 1000, message = "Description must not exceed 1000 characters")
	private String description;

	private Priority priority;

	private Integer storyPoints;

	@Size(max = 500, message = "Acceptance criteria must not exceed 500 characters")
	private String acceptanceCriteria;
}
