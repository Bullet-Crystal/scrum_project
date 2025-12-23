package org.example.service;

import java.util.List;

import org.example.model.Epic;
import org.example.model.UserStory;

public interface EpicService {

	Epic getEpicById(Long epicId);

	Epic createEpic(Long productBacklogId, Epic epic);

	void updateEpic(Long oldEpicId, Epic newEpic);

	void deleteEpic(Long epicId);

	void addUserStoryToEpic(Long epicId, Long userStoryId);

	void removeUserStoryFromEpic(Long epicId, Long userStoryId);

	List<UserStory> getUserStoriesFromEpic(Long epicId);

	String vizualizeUserStoriesFromEpic(Long epicId);
}
