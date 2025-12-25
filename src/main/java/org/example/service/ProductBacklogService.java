package org.example.service;

import org.example.model.Priority;
import org.example.model.ProductBacklog;
import org.example.model.UserStory;

public interface ProductBacklogService {
	ProductBacklog getProductBacklogById(Long id);

	ProductBacklog createProductBacklog(String title);

	void addUserStoryToBacklog(Long backlogId, UserStory userStory);

	void deleteUserStoryFromBacklog(Long backlogId, Long userStoryId);

	void updateUserStoryFromBacklog(Long backlogId, Long userStoryId, UserStory newUserStory);

	void prioritizeUserStoryFromBacklog(Long backlogId, Long userStoryId, Priority priority);
}
