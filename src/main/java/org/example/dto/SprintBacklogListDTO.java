
package org.example.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SprintBacklogListDTO {
	private Long id;
	private String nom;
	private LocalDate dateDebut;
	private LocalDate dateFin;

}
