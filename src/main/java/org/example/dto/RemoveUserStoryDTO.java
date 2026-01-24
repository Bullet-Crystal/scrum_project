
package org.example.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RemoveUserStoryDTO {
	@NonNull
	private Long userStoryId;
}
