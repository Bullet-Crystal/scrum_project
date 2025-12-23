package org.example.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Entity
@AllArgsConstructor
@Builder
public class ProductBacklog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Id;
	private String title;
	private Priority priority;

	@OneToOne(mappedBy = "productBacklog", cascade = CascadeType.ALL)
	private Project project;
	@OneToMany(mappedBy = "productBacklog", cascade = CascadeType.ALL)
	private List<UserStory> userStories = new ArrayList<>();
	@OneToMany(mappedBy = "productBacklog", cascade = CascadeType.ALL)
	private List<Epic> epics = new ArrayList<>();

	public ProductBacklog() {
		this.setTitle("The Product Backlog");
	}

	public void addUserStory(UserStory story) {
		story.setProductBacklog(this);
		this.userStories.add(story);
	}

	public void removeUserStory(UserStory story) {
		story.setProductBacklog(null);
		this.userStories.remove(story);
	}

	public void addEpic(Epic epic) {
		epic.setProductBacklog(this);
		this.epics.add(epic);
	}
}
