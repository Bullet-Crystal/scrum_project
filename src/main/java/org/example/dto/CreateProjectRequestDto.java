package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateProjectRequestDto {
	@NotBlank(message = "Project title is required")
	@Size(max = 100, message = "Project title must not exceed 100 characters")
	private String title;

	@Size(max = 500, message = "Description must not exceed 500 characters")
	private String description;
}
