package org.example.service;

import java.util.List;

import org.example.model.Task;
import org.example.model.UserStory;

public interface SprintBacklogService {
	List<UserStoryService> getSprintUserStories();

	void addSprintUserStories(UserStory userStory);

	void removeSprintUserStories(UserStory userStory);

	void removeSprintTask(Task task);

}
