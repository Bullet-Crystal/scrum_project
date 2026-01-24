
package org.example.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class SprintBacklogUpdateDTO {
	private String nom;
	private String objectif;
	private LocalDate dateDebut;
	private LocalDate dateFin;
}
