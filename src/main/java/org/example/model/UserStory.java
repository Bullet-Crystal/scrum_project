package org.example.model;

import lombok.*;

import java.util.Date;

import jakarta.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserStory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Id;

	private String title;
	private String description;
	private Priority priority;
	private Statut statut = Statut.TO_DO; // par defaut user story en todo
	private String critereAcceptation;
	private Date creationDate;

	// TODO: find the relation between productBacklog and userStory
	@ManyToOne
	@JoinColumn(name = "product_backlog_id")
	private ProductBacklog productBacklog;
	@ManyToOne
	@JoinColumn(name = "epic_id")
	private Epic epic;

	@ManyToOne
	@JoinColumn(name = "sprint_backlog_id")
	private SprintBacklog sprintBacklog;

}
