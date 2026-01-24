package org.example.dto;

import java.time.LocalDate;

import org.example.model.Status;
import org.example.model.Priority;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserStoryResponseDto {
	private Long id;
	private String title;
	private String role;
	private String action;
	private String goal;
	private Priority priority;
	private Integer storyPoints;
	private Status status;
	private LocalDate creationDate;
}
