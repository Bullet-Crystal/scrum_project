package org.example.dto;

import java.util.Date;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateEpicRequestDto {
	@Size(max = 200, message = "Title must not exceed 200 characters")
	private String title;

	private String goal;

	private Date targetDate;
}
