
package org.example.dto;

import lombok.*;
import org.example.model.Status;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class TaskStatusDTO {
	private Status status;
}
