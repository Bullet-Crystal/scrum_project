package org.example.dto;

import org.example.model.Priority;
import org.example.model.Status;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserStoryDto {
	@NotBlank(message = "User story title is required")
	@Size(max = 200, message = "Title must not exceed 200 characters")
	private String title;

	private String role;
	private String action;
	private String goal;
	private Status userStoryStatut;
	private String critereAcceptation;

	@NotNull(message = "Priority is required")
	private Priority priority;

	@Size(max = 500, message = "Acceptance criteria must not exceed 500 characters")
	private String acceptanceCriteria;
}
