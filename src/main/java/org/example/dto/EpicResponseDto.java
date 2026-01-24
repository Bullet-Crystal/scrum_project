package org.example.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EpicResponseDto {
	private Long id;
	private String title;
	private String description;
	private String goal;
	private LocalDate targetDate;
	private LocalDate creationDate;
	private List<UserStoryResponseDto> userStories;
}
