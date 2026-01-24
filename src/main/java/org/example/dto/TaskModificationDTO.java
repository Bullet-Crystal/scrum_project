
package org.example.dto;

import lombok.*;
import org.example.model.Status;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class TaskModificationDTO {
	private String titre;
	private String description;
	private Status status;
}
