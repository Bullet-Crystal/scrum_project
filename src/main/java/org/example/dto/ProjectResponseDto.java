package org.example.dto;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectResponseDto {
	private Long id;
	private String title;
	private LocalDate creationDate;
	private ProductBacklogResponseDto productBacklog;
}
