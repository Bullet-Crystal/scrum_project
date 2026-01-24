package org.example.service;

import org.example.model.Priority;
import org.example.model.ProductBacklog;
import org.example.model.UserStory;
import org.example.repository.ProductBacklogRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductBacklogServiceImp implements ProductBacklogService {
	private final ProductBacklogRepository productBacklogRepo;
	private final ProjectService projectService;
	private final UserStoryService userStoryService;

	@Override
	public ProductBacklog getProductBacklogById(Long id) {
		return productBacklogRepo.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("ProductBacklog not Found !"));
	}

	@Override
	public ProductBacklog createProductBacklog(String title) {
		ProductBacklog backlog = ProductBacklog.builder()
				.title(title)
				.build();
		return productBacklogRepo.save(backlog);
	}

	public void isAuthorizedInBacklog(Long backlogId, String username) {
		ProductBacklog backlog = getProductBacklogById(backlogId);
		projectService.isUserAuthorized(backlog.getProject().getId(), username);
	}

	@Transactional
	@Override
	public void addUserStoryToBacklog(Long backlogId, UserStory userStory) {
		ProductBacklog backlog = getProductBacklogById(backlogId);

		if (userStoryService.existsByTitle(userStory.getTitle())) {
			throw new IllegalStateException("User Story already exists");
		}
		backlog.addUserStory(userStory);
	}

	@Transactional
	@Override
	public void deleteUserStoryFromBacklog(Long backlogId, Long userStoryId) {
		ProductBacklog backlog = getProductBacklogById(backlogId);
		UserStory userStory = userStoryService.getUserStoryById(userStoryId);

		if (!backlog.equals(userStory.getProductBacklog())) {
			throw new IllegalStateException("UserStory does not belong to this backlog");
		}
		backlog.removeUserStory(userStory);
		userStoryService.deleteUserStory(userStory);
	}

	@Transactional
	@Override
	public void updateUserStoryFromBacklog(Long backlogId, Long userStoryId, UserStory newUserStory) {
		ProductBacklog backlog = getProductBacklogById(backlogId);
		UserStory userStory = userStoryService.getUserStoryById(userStoryId);

		if (!backlog.equals(userStory.getProductBacklog())) {
			throw new IllegalStateException("UserStory does not belong to this backlog");
		}
		userStoryService.updateUserStory(userStory, newUserStory);
	}

	@Transactional
	@Override
	public void prioritizeUserStoryFromBacklog(Long backlogId, Long userStoryId, Priority priority) {
		UserStory userStory = userStoryService.getUserStoryById(userStoryId);
		ProductBacklog backlog = getProductBacklogById(backlogId);

		if (!backlog.equals(userStory.getProductBacklog())) {
			throw new IllegalStateException("UserStory does not belong to this backlog");
		}
		userStory.setPriority(priority);
	}

}
