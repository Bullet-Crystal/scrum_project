package org.example.service;

import java.util.List;

import org.example.model.SprintBacklog;
import org.example.model.Task;
import org.example.model.UserStory;

public class SprintBacklogServiceImp implements SprintBacklogService {
	private SprintBacklog sprintBacklog;

	@Override
	public List<UserStoryService> getSprintUserStories() {
		return List.of();
	}

	@Override
	public void addSprintUserStories(UserStory userStory) {

	}

	@Override
	public void removeSprintUserStories(UserStory userStory) {

	}

	@Override
	public void removeSprintTask(Task task) {

	}
}
