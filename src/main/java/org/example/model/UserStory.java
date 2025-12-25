package org.example.model;

import lombok.*;

import java.util.Date;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
public class UserStory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String title; // should be title
	private String role;
	private String action;
	private String goal;
	private Priority priority;
	@Enumerated(EnumType.STRING)
	private Statut userStoryStatut = Statut.TO_DO; // par defaut user story en todo
	private String critereAcceptation;
	@OneToMany(mappedBy = "userStory", cascade = CascadeType.ALL)
	private List<Task> tasks = new ArrayList<>();
	@ManyToOne
	@JoinColumn(name = "epic_id")
	Epic epic;
	@ManyToOne
	@JoinColumn(name = "product_backlog_id")
	ProductBacklog productBacklog;
	@ManyToOne
	@JoinColumn(name = "sprint_backlog_id")
	SprintBacklog sprintBacklog;

	public String getDescription() {
		return "En tant que " + role + ", je veux " + action + " afin de " + goal;
	}

	public void setDescription(String role, String action, String goal) {
		this.role = role;
		this.action = action;
		this.goal = goal;
	}

	public void addTask(Task task) {
		this.tasks.add(task);
	}

	public void removeTask(Task task) {
		this.tasks.remove(task);
	}

	public Boolean containsTask(Task task) {
		return this.tasks.contains(task);
	}
}
