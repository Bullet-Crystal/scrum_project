
package org.example.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class SprintBacklogResponseDTO {
	private Long id;
	private String nom;
	private LocalDate dateDebut;
	private LocalDate dateFin;
	List<UserStoryListDto> userStories = new ArrayList<>();
	List<TaskListDTO> tasks = new ArrayList<>();

}
