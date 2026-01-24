package org.example.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateEpicRequestDto {
	@NotBlank(message = "Epic title is required")
	@Size(max = 200, message = "Title must not exceed 200 characters")
	private String title;

	@Size(max = 1000, message = "Description must not exceed 1000 characters")
	private String description;

	private String goal;

	private LocalDate targetDate;
}
