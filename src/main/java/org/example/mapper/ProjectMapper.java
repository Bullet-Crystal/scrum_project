package org.example.mapper;

import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;
import org.example.dto.ProjectResponseDto;
import org.example.model.Project;
import org.springframework.stereotype.Component;

@Component
public class ProjectMapper {

	private ProductBacklogMapper productBacklogMapper;

	ProjectMapper(ProductBacklogMapper productBacklogMapper) {
		this.productBacklogMapper = productBacklogMapper;
	}

	/**
	 * Convert Project entity to ProjectResponse DTO
	 */
	public ProjectResponseDto toProjectResponse(Project project) {
		if (project == null) {
			return null;
		}

		return ProjectResponseDto.builder()
				.id(project.getId())
				.title(project.getTitle())
				.creationDate(project.getCreationDate())
				.productBacklog(productBacklogMapper
						.toProductBacklogResponse(project.getProductBacklog()))
				.build();
	}

	/**
	 * Convert list of Project entities to list of ProjectResponse DTOs
	 */
	public List<ProjectResponseDto> toProjectResponseList(List<Project> projects) {
		if (projects == null) {
			return new ArrayList<>();
		}

		return projects.stream()
				.map(this::toProjectResponse)
				.collect(Collectors.toList());
	}

}
