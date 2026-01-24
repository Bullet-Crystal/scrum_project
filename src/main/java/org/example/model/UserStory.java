package org.example.model;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

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
	private String title;
	private String role;
	private String action;
	private String goal;
	private Priority priority;
	@CreationTimestamp
	private LocalDate creationDate;
	@Enumerated(EnumType.STRING)
	private Status status;
	private String critereAcceptation;
	@OneToMany(mappedBy = "userStory", cascade = CascadeType.ALL)
	private List<Task> tasks;
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
