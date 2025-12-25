package org.example.service;

import org.example.model.ProductBacklog;
import org.example.model.SprintBacklog;
import org.example.model.Task;
import org.example.model.UserStory;
import org.example.service.ProductBacklogService;
import org.example.service.SprintBacklogServiceImp;
import org.example.service.UserStoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.example.repository.SprintBacklogRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class SprintBacklogServiceTest {
	@Mock
	private SprintBacklogRepository sprintBacklogRepository;
	@Mock
	private UserStoryService userStoryService;

	@Mock
	private ProductBacklogService productBacklogService;

	@InjectMocks
	private SprintBacklogServiceImp sprintBacklogService;
	private UserStory userStory;
	private SprintBacklog sprintBacklog;
	private ProductBacklog productBacklog;

	@BeforeEach
	void setUp() {
		productBacklog = mock(ProductBacklog.class);
		userStory = mock(UserStory.class);
		sprintBacklog = spy(new SprintBacklog());
	}

	@Test
	void shouldAddUserStoryWhenProductBacklogContainsItAndSprintExists() {
		Long sprintId = 1L;
		Long productBacklogId = 2L;

		when(productBacklogService.getProductBacklogById(productBacklogId))
				.thenReturn(productBacklog);
		when(productBacklog.containsUserStory(userStory))
				.thenReturn(true);
		when(sprintBacklogRepository.findById(sprintId))
				.thenReturn(Optional.of(sprintBacklog));

		sprintBacklogService.addSprintUserStories(sprintId, productBacklogId, userStory);

		verify(sprintBacklog).addUserStory(userStory);
		verify(sprintBacklogRepository).save(sprintBacklog);
	}

	@Test
	void shouldDoNothingWhenUserStoryNotInProductBacklog() {
		Long sprintId = 1L;
		Long productBacklogId = 2L;

		when(productBacklogService.getProductBacklogById(productBacklogId))
				.thenReturn(productBacklog);
		when(productBacklog.containsUserStory(userStory))
				.thenReturn(false);

		sprintBacklogService.addSprintUserStories(sprintId, productBacklogId, userStory);

		verify(sprintBacklogRepository, never()).findById(any());
		verify(sprintBacklogRepository, never()).save(any());
	}

	@Test
	void shouldThrowExceptionWhenSprintBacklogNotFound() {
		Long sprintId = 1L;
		Long productBacklogId = 2L;

		when(productBacklogService.getProductBacklogById(productBacklogId))
				.thenReturn(productBacklog);
		when(productBacklog.containsUserStory(userStory))
				.thenReturn(true);
		when(sprintBacklogRepository.findById(sprintId))
				.thenReturn(Optional.empty());

		IllegalArgumentException exception = assertThrows(
				IllegalArgumentException.class,
				() -> sprintBacklogService.addSprintUserStories(sprintId, productBacklogId, userStory));

		assertEquals("Sprint backlog not found", exception.getMessage());
		verify(sprintBacklogRepository, never()).save(any());
	}

	@Test
	void shouldThrowExceptionWhenProductBacklogNotFound() {
		Long sprintId = 1L;
		Long productBacklogId = 2L;

		when(productBacklogService.getProductBacklogById(productBacklogId))
				.thenThrow(new IllegalArgumentException("Product backlog not found"));

		assertThrows(
				IllegalArgumentException.class,
				() -> sprintBacklogService.addSprintUserStories(sprintId, productBacklogId, userStory));

		verify(sprintBacklogRepository, never()).findById(any());
		verify(sprintBacklogRepository, never()).save(any());
	}

	// test pour removeSprintUserStories
	@Test
	void shouldDoNothingWhenUserStoryIsNotAccepted() {
		when(userStoryService.isAccepted(userStory)).thenReturn(false);
		sprintBacklogService.removeSprintUserStories(1L, userStory);
		verify(sprintBacklogRepository, never()).findById(anyLong());
		verify(sprintBacklogRepository, never()).save(any());
	}

	@Test
	void shouldThrowExceptionWhenUserStoryIsNotFound() {
		when(userStoryService.isAccepted(userStory)).thenReturn(true);
		sprintBacklog.setId(1L);
		when(sprintBacklogRepository.findById(1L)).thenReturn(Optional.empty());
		assertThrows(IllegalArgumentException.class,
				() -> sprintBacklogService.removeSprintUserStories(1L, userStory));
	}

	@Test
	void shouldRemoveUserStoryAndSaveSprintBacklog() {
		when(userStoryService.isAccepted(userStory)).thenReturn(true);
		when(sprintBacklogRepository.findById(1L)).thenReturn(Optional.of(sprintBacklog));

		sprintBacklogService.removeSprintUserStories(1L, userStory);

		verify(sprintBacklog).removeUserStory(userStory);
		verify(sprintBacklogRepository).save(sprintBacklog);
	}

	// test methode : removeSprintTask

	@Test
	void shouldThrowExceptionWhenRemovingTaskFromNonExistingSprintBacklog() {
		when(sprintBacklogRepository.findById(1L)).thenReturn(Optional.empty());

		assertThrows(IllegalArgumentException.class,
				() -> sprintBacklogService.removeSprintTask(1L, new Task()));
	}

	@Test
	void shouldRemoveTaskAndSaveSprintBacklog() {
		Task task = new Task();
		SprintBacklog sprintBacklog = spy(new SprintBacklog());

		when(sprintBacklogRepository.findById(1L)).thenReturn(Optional.of(sprintBacklog));

		sprintBacklogService.removeSprintTask(1L, task);

		verify(sprintBacklog).removeTask(task);
		verify(sprintBacklogRepository).save(sprintBacklog);
	}

	// test methode : addSprintTask
	@Test
	void shouldThrowExceptionWhenSprintBacklogDoesNotExist() {

		when(sprintBacklogRepository.findById(1L)).thenReturn(Optional.empty());

		assertThrows(IllegalArgumentException.class,
				() -> sprintBacklogService.addSprintTask(1L, 2L, new Task()));

		verify(userStoryService, never()).getUserStoryById(anyLong());

	}

	@Test
	void shouldDoNothingWhenTaskAlreadyExistsInUserStory() {
		Task task = new Task();
		UserStory userStory = mock(UserStory.class);
		SprintBacklog sprintBacklog = mock(SprintBacklog.class);

		when(sprintBacklogRepository.findById(1L)).thenReturn(Optional.of(sprintBacklog));
		when(userStoryService.getUserStoryById(2L)).thenReturn(userStory);
		when(userStory.containsTask(task)).thenReturn(true);

		sprintBacklogService.addSprintTask(1L, 2L, task);

		verify(userStory, never()).addTask(any());
		verify(sprintBacklog, never()).addTask(any());
		verify(userStoryService, never()).addUserStory(any());
		verify(sprintBacklogRepository, never()).save(any());
	}

	@Test

	void shouldAddTaskAndSaveUserStoryAndSprintBacklog() {
		Task task = new Task();
		UserStory userStory = mock(UserStory.class);
		SprintBacklog sprintBacklog = mock(SprintBacklog.class);

		when(sprintBacklogRepository.findById(1L)).thenReturn(Optional.of(sprintBacklog));
		when(userStoryService.getUserStoryById(2L)).thenReturn(userStory);
		when(userStory.containsTask(task)).thenReturn(false);

		sprintBacklogService.addSprintTask(1L, 2L, task);

		verify(userStory, times(1)).addTask(task);
		verify(sprintBacklog, times(1)).addTask(task);
		verify(userStoryService, times(1)).addUserStory(userStory);
		verify(sprintBacklogRepository, times(1)).save(sprintBacklog);
	}

}
