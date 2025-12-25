package org.example.service;

import org.example.model.ProductBacklog;
import org.example.repository.SprintBacklogRepository;
import org.example.model.SprintBacklog;
import org.example.model.Task;
import org.example.model.UserStory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor

public class SprintBacklogServiceImp implements SprintBacklogService {

	private final SprintBacklogRepository sprintBacklogRepository;
	private final UserStoryService userStoryService;
	private final ProductBacklogService productBacklogService;

	@Override
	@Transactional
	public void addSprintUserStories(Long sprintId, Long productBacklogId, UserStory userStory) {
		ProductBacklog productBacklog = productBacklogService.getProductBacklogById(productBacklogId);
		if (productBacklog.containsUserStory(userStory)) {
			SprintBacklog sprintBacklog = sprintBacklogRepository.findById(sprintId)
					.orElseThrow(() -> new IllegalArgumentException("Sprint backlog not found"));
			sprintBacklog.addUserStory(userStory);
			sprintBacklogRepository.save(sprintBacklog);
		}
	}

	// test verifier
	@Override
	@Transactional
	public void removeSprintUserStories(Long sprintId, UserStory userStory) {
		if (!userStoryService.isAccepted(userStory)) {
			return;
		}
		SprintBacklog sprintBacklog = sprintBacklogRepository.findById(sprintId)
				.orElseThrow(() -> new IllegalArgumentException("Sprint backlog not found"));
		sprintBacklog.removeUserStory(userStory);
		sprintBacklogRepository.save(sprintBacklog);
	}

	// test verifie
	@Override
	@Transactional
	public void addSprintTask(Long sprintId, Long userStoryId, Task task) {
		SprintBacklog sprintBacklog = sprintBacklogRepository.findById(sprintId)
				.orElseThrow(() -> new IllegalArgumentException("Sprint backlog not found"));
		UserStory userStory = userStoryService.getUserStoryById(userStoryId);
		if (!userStory.containsTask(task)) {
			userStory.addTask(task);
			sprintBacklog.addTask(task);
			userStoryService.addUserStory(userStory);
			sprintBacklogRepository.save(sprintBacklog);
		}
	}

	// test verifie
	@Override
	@Transactional
	public void removeSprintTask(Long sprintId, Task task) {
		SprintBacklog sprintBacklog = sprintBacklogRepository.findById(sprintId)
				.orElseThrow(() -> new IllegalArgumentException("Sprint backlog not found"));
		sprintBacklog.removeTask(task);

		sprintBacklogRepository.save(sprintBacklog);
	}

}
