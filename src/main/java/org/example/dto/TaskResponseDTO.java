
package org.example.dto;

import lombok.*;
import org.example.model.Status;
import org.example.model.UserStory;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class TaskResponseDTO {
	private Long id;
	private String titre;
	private String description;
	private Status taskStatus;
	private UserStory userStory;

}
