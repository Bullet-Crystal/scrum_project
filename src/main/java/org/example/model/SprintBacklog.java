package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

import jakarta.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SprintBacklog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String title;
	private Date startDate;
	private Date endDate;
	private Statut statut;

	@ManyToOne
	@JoinColumn(name = "project_id")
	private Project project;
	@OneToMany(mappedBy = "sprintBacklog")
	private List<UserStory> userStories;
}
