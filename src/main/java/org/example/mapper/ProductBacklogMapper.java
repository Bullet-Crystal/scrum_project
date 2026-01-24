package org.example.mapper;

import org.example.dto.ProductBacklogResponseDto;
import org.example.model.ProductBacklog;
import org.springframework.stereotype.Component;

@Component
public class ProductBacklogMapper {

	/**
	 * Convert ProductBacklog entity to ProductBacklogResponse DTO
	 */
	public ProductBacklogResponseDto toProductBacklogResponse(ProductBacklog productBacklog) {
		if (productBacklog == null) {
			return null;
		}

		return ProductBacklogResponseDto.builder()
				.id(productBacklog.getId())
				.title(productBacklog.getTitle())
				.projectId(productBacklog.getProject().getId())
				.build();
	}
}
