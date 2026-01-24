
package org.example.dto;

import lombok.*;
import org.example.model.Priority;
import org.example.model.Status;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddUserStoryToSprintDTO {
	@NonNull
	private Long userStoryId;

	@NonNull
	private Long productBacklogId;

	@NonNull
	private String titre;

	private String description;

	private Priority priority;
	private String role;
	private String action;
	private String goal;
	private Status status;

}
