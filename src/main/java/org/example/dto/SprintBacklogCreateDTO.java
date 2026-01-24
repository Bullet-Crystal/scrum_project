
package org.example.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.example.model.Task;
import org.example.model.UserStory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class SprintBacklogCreateDTO {
	private Long id;
	private String nom;
	private LocalDate dateDebut;
	private LocalDate dateFin;
	List<UserStory> userStories = new ArrayList<>();
	List<Task> tasks = new ArrayList<>();
}
