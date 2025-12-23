package org.example.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.example.model.Priority;
import org.example.model.ProductBacklog;
import org.example.model.UserStory;
import org.example.repository.ProductBacklogRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class ProductBacklogServiceTest {

	@Mock
	private ProductBacklogRepository productBacklogRepo;

	@Mock
	private UserStoryService userStoryService;

	@InjectMocks
	private ProductBacklogServiceImp service;

	// --------------------------------------------------
	// getProductBacklogById
	// --------------------------------------------------

	@Test
	void getProductBacklogById_shouldReturnBacklog_whenExists() {
		ProductBacklog backlog = new ProductBacklog();
		backlog.setId(1L);

		when(productBacklogRepo.findById(1L))
				.thenReturn(Optional.of(backlog));

		ProductBacklog result = service.getProductBacklogById(1L);

		assertThat(result).isEqualTo(backlog);
	}

	@Test
	void getProductBacklogById_shouldThrowException_whenNotFound() {
		when(productBacklogRepo.findById(1L))
				.thenReturn(Optional.empty());

		assertThatThrownBy(() -> service.getProductBacklogById(1L))
				.isInstanceOf(EntityNotFoundException.class)
				.hasMessageContaining("ProductBacklog not Found");
	}

	// --------------------------------------------------
	// addUserStoryToBacklog
	// --------------------------------------------------

	@Test
	void addUserStoryToBacklog_shouldAddUserStory_whenTitleDoesNotExist() {
		ProductBacklog backlog = new ProductBacklog();
		backlog.setId(1L);

		UserStory userStory = new UserStory();
		userStory.setTitle("Login feature");

		when(productBacklogRepo.findById(1L))
				.thenReturn(Optional.of(backlog));
		when(userStoryService.existsByTitle("Login feature"))
				.thenReturn(false);

		service.addUserStoryToBacklog(1L, userStory);

		assertThat(backlog.getUserStories()).contains(userStory);
	}

	@Test
	void addUserStoryToBacklog_shouldThrowException_whenUserStoryExists() {
		ProductBacklog backlog = new ProductBacklog();
		backlog.setId(1L);

		UserStory userStory = new UserStory();
		userStory.setTitle("Login feature");

		when(productBacklogRepo.findById(1L))
				.thenReturn(Optional.of(backlog));
		when(userStoryService.existsByTitle("Login feature"))
				.thenReturn(true);

		assertThatThrownBy(() -> service.addUserStoryToBacklog(1L, userStory))
				.isInstanceOf(IllegalStateException.class)
				.hasMessageContaining("User Story already exists");

		verify(userStoryService, never()).addUserStory(any());
	}

	// --------------------------------------------------
	// deleteUserStoryFromBacklog
	// --------------------------------------------------

	@Test
	void deleteUserStoryFromBacklog_shouldDelete_whenBelongsToBacklog() {
		ProductBacklog backlog = new ProductBacklog();
		backlog.setId(1L);

		UserStory userStory = new UserStory();
		userStory.setId(10L);
		userStory.setProductBacklog(backlog);

		when(productBacklogRepo.findById(1L))
				.thenReturn(Optional.of(backlog));
		when(userStoryService.getUserStoryById(10L))
				.thenReturn(userStory);

		service.deleteUserStoryFromBacklog(1L, 10L);

		verify(userStoryService).deleteUserStory(userStory);
	}

	@Test
	void deleteUserStoryFromBacklog_shouldThrowException_whenNotBelonging() {
		ProductBacklog backlog = new ProductBacklog();
		backlog.setId(1L);

		ProductBacklog otherBacklog = new ProductBacklog();
		otherBacklog.setId(2L);

		UserStory userStory = new UserStory();
		userStory.setId(10L);
		userStory.setProductBacklog(otherBacklog);

		when(productBacklogRepo.findById(1L))
				.thenReturn(Optional.of(backlog));
		when(userStoryService.getUserStoryById(10L))
				.thenReturn(userStory);

		assertThatThrownBy(() -> service.deleteUserStoryFromBacklog(1L, 10L))
				.isInstanceOf(IllegalStateException.class)
				.hasMessageContaining("does not belong");

		verify(userStoryService, never()).deleteUserStory(any());
	}

	// --------------------------------------------------
	// updateUserStoryFromBacklog
	// --------------------------------------------------

	@Test
	void updateUserStoryFromBacklog_shouldUpdate_whenBelongsToBacklog() {
		ProductBacklog backlog = new ProductBacklog();
		backlog.setId(1L);

		UserStory oldStory = new UserStory();
		oldStory.setId(10L);
		oldStory.setProductBacklog(backlog);

		UserStory newStory = new UserStory();

		when(productBacklogRepo.findById(1L))
				.thenReturn(Optional.of(backlog));
		when(userStoryService.getUserStoryById(10L))
				.thenReturn(oldStory);

		service.updateUserStoryFromBacklog(1L, 10L, newStory);

		verify(userStoryService).updateUserStory(oldStory, newStory);
	}

	// --------------------------------------------------
	// prioritizeUserStoryFromBacklog
	// --------------------------------------------------

	@Test
	void prioritizeUserStoryFromBacklog_shouldSetPriority_whenBelongsToBacklog() {
		ProductBacklog backlog = new ProductBacklog();
		backlog.setId(1L);

		UserStory userStory = new UserStory();
		userStory.setId(10L);
		userStory.setProductBacklog(backlog);

		when(productBacklogRepo.findById(1L))
				.thenReturn(Optional.of(backlog));
		when(userStoryService.getUserStoryById(10L))
				.thenReturn(userStory);

		service.prioritizeUserStoryFromBacklog(1L, 10L, Priority.COULD_HAVE);

		assertThat(userStory.getPriority()).isEqualTo(Priority.COULD_HAVE);
	}
}
