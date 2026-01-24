
package org.example.dto;

import lombok.*;
import org.example.model.Status;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AddTaskToSprintDTO {
	private Long userStoryId;
	private String titre;
	private String description;
	private Status status;
}
