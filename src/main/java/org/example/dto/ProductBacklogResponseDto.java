package org.example.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductBacklogResponseDto {
	private Long id;
	private String title;
	private Long projectId;
}
