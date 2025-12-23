package org.example.service;

import java.util.List;

import org.example.model.Statut;
import org.example.model.Task;
import org.example.model.UserStory;

public interface UserStoryService {

	UserStory getUserStoryById(Long userStoryId);

	void addUserStory(UserStory userStory);

	Boolean existsByTitle(String title);

	void updateUserStory(UserStory oldUserStory, UserStory newUserStory);

	void deleteUserStory(UserStory u);

	List<Task> getTasks();

	Statut getStatus(UserStory u);

	Boolean isAccepted(UserStory u);

}
