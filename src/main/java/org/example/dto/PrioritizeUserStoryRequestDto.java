package org.example.dto;

import org.example.model.Priority;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data

public class PrioritizeUserStoryRequestDto {
	@NotNull(message = "Priority is required")
	private Priority priority;
}
