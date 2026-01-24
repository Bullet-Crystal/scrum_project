
package org.example.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class RemoveTaskDTO {

	@NonNull
	private Long taskId;
}
