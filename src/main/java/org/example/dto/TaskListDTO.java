
package org.example.dto;

import lombok.*;
import org.example.model.UserStory;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TaskListDTO {
	private Long id;
	private String titre;
	private String description;
	private UserStory userStory;
}
