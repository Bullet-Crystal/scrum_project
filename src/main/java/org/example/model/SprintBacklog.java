package org.example.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class SprintBacklog {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nom;
	private LocalDate dateDebut;
	private LocalDate dateFin;
	@ManyToOne
	@JoinColumn(name = "project_id")
	private Project project;
	@OneToMany(mappedBy = "sprintBacklog", cascade = CascadeType.ALL)
	List<UserStory> userStories;
	@OneToMany(mappedBy = "sprintBacklog", cascade = CascadeType.ALL)
	List<Task> tasks;

	public void addUserStory(UserStory userStory) {
		if (this.userStories == null) {
			this.userStories = new ArrayList<>();
		}
		this.userStories.add(userStory);
	}

	public void addTask(Task task) {
		if (this.tasks == null) {
			this.tasks = new ArrayList<>();
		}
		this.tasks.add(task);
	}

	public void removeUserStory(UserStory userStory) {
		if (this.userStories == null) {
			return;
		}
		this.userStories.remove(userStory);

	}

	public void removeTask(Task task) {
		if (this.tasks == null) {
			return;
		}
		this.tasks.remove(task);
	}
}
