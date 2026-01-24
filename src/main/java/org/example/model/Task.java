package org.example.model;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Task {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String title;
	private String description;
	@ManyToOne
	@JoinColumn(name = "user_story_id")
	UserStory userStory;
	Status status = Status.TO_DO;
	@ManyToOne
	@JoinColumn(name = "sprint_backlog_id")
	SprintBacklog sprintBacklog;
}
