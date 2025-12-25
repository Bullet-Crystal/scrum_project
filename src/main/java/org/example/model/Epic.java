package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
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
	@CreationTimestamp
	private Date creationDate;
	@OneToMany(mappedBy = "epic")
	List<UserStory> userStories = new ArrayList<>();

	@ManyToOne
	@JoinColumn(name = "product_backlog_id")
	private ProductBacklog productBacklog;

	public void addUserStory(UserStory userStory) {
		userStory.setEpic(this);
		this.getUserStories().add(userStory);
	}

	public void removeUserStory(UserStory userStory) {
		userStory.setEpic(null);
		this.getUserStories().remove(userStory);
	}
}
