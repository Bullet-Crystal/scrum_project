
package org.example.dto;

import lombok.*;
import org.example.model.Epic;
import org.example.model.Priority;
import org.example.model.ProductBacklog;
import org.example.model.Status;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserStoryListDto {
	private Long id;
	private String titre;
	private String description;
	private Priority priorite;
	private Status status;
	private Epic epic;
	private ProductBacklog productBacklog;
}
