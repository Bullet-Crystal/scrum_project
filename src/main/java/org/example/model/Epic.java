package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Epic {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Id;
	private String title;
	private String description;
	private String goal;
	@CreationTimestamp
	private LocalDate creationDate;
	private LocalDate targetDate;
	@OneToMany(mappedBy = "epic")
	List<UserStory> userStories;

	@ManyToOne
	@JoinColumn(name = "product_backlog_id")
	private ProductBacklog productBacklog;

	public void addUserStory(UserStory userStory) {
		userStory.setEpic(this);
		if (this.userStories == null) {
			this.userStories = new ArrayList<>();
		}
		this.getUserStories().add(userStory);
	}

	public void removeUserStory(UserStory userStory) {
		userStory.setEpic(null);
		this.getUserStories().remove(userStory);
	}
}
