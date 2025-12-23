package org.example.service;

import java.util.List;

import org.example.model.Epic;
import org.example.model.ProductBacklog;
import org.example.model.UserStory;
import org.example.repository.EpicRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class EpicServiceImp implements EpicService {

	private final EpicRepository epicRepo;
	private final ProductBacklogService productBacklogService;
	private final UserStoryService userStoryService;

	@Transactional
	@Override
	public Epic getEpicById(Long epicId) {
		return epicRepo.findById(epicId)
				.orElseThrow(() -> new EntityNotFoundException("Epic Not Found ! "));
	}

	@Transactional
	@Override
	public Epic createEpic(Long productBacklogId, Epic epic) {
		ProductBacklog productBacklog = productBacklogService.getProductBacklogById(productBacklogId);
		productBacklog.addEpic(epic);
		return epicRepo.save(epic);
	}

	@Transactional
	@Override
	public void updateEpic(Long oldEpicId, Epic newEpic) {
		Epic epic = getEpicById(oldEpicId);
		epic.setTitle(newEpic.getTitle());
		epic.setDescription(newEpic.getDescription());
	}

	@Transactional
	@Override
	public void deleteEpic(Long epicId) {
		Epic epic = getEpicById(epicId);
		epicRepo.delete(epic);
	}

	@Transactional
	@Override
	public void addUserStoryToEpic(Long epicId, Long userStoryId) {
		Epic epic = getEpicById(epicId);
		UserStory userStory = userStoryService.getUserStoryById(userStoryId);
		epic.addUserStory(userStory);
	}

	@Transactional
	@Override
	public void removeUserStoryFromEpic(Long epicId, Long userStoryId) {
		Epic epic = getEpicById(epicId);
		UserStory userStory = userStoryService.getUserStoryById(userStoryId);
		epic.removeUserStory(userStory);
	}

	@Transactional
	@Override
	public List<UserStory> getUserStoriesFromEpic(Long epicId) {
		Epic epic = getEpicById(epicId);
		return epic.getUserStories();
	}

	@Transactional
	@Override
	public String vizualizeUserStoriesFromEpic(Long epicId) {
		List<UserStory> userStories = getUserStoriesFromEpic(epicId);
		String builtString = "";
		for (UserStory userStory : userStories) {
			builtString += userStory.getTitle() + "\n";
		}
		return builtString;
	}
}
