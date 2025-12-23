package org.example.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.example.model.Epic;
import org.example.model.ProductBacklog;
import org.example.model.UserStory;
import org.example.repository.EpicRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class EpicServiceTest {

	@Mock
	private EpicRepository epicRepo;

	@Mock
	private ProductBacklogService productBacklogService;

	@Mock
	private UserStoryService userStoryService;

	@InjectMocks
	private EpicServiceImp epicService;

	// ----------------------------
	// getEpicById
	// ----------------------------

	@Test
	void getEpicById_shouldReturnEpic_whenExists() {
		Epic epic = new Epic();
		epic.setId(1L);

		when(epicRepo.findById(1L)).thenReturn(Optional.of(epic));

		Epic result = epicService.getEpicById(1L);

		assertThat(result).isEqualTo(epic);
	}

	@Test
	void getEpicById_shouldThrow_whenNotFound() {
		when(epicRepo.findById(1L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> epicService.getEpicById(1L))
				.isInstanceOf(EntityNotFoundException.class)
				.hasMessageContaining("Epic Not Found");
	}

	// ----------------------------
	// createEpic
	// ----------------------------

	@Test
	void createEpic_shouldAddEpicToProductBacklog_andSave() {
		ProductBacklog backlog = new ProductBacklog();
		Epic epic = new Epic();

		when(productBacklogService.getProductBacklogById(1L))
				.thenReturn(backlog);
		when(epicRepo.save(epic)).thenReturn(epic);

		Epic savedEpic = epicService.createEpic(1L, epic);

		assertThat(savedEpic).isEqualTo(epic);
		assertThat(backlog.getEpics()).contains(epic);
		verify(epicRepo).save(epic);
	}

	// ----------------------------
	// updateEpic
	// ----------------------------

	@Test
	void updateEpic_shouldUpdateFields() {
		Epic existingEpic = new Epic();
		existingEpic.setTitle("Old title");
		existingEpic.setDescription("Old desc");

		Epic newEpic = new Epic();
		newEpic.setTitle("New title");
		newEpic.setDescription("New desc");

		when(epicRepo.findById(1L))
				.thenReturn(Optional.of(existingEpic));

		epicService.updateEpic(1L, newEpic);

		assertThat(existingEpic.getTitle()).isEqualTo("New title");
		assertThat(existingEpic.getDescription()).isEqualTo("New desc");
	}

	// ----------------------------
	// deleteEpic
	// ----------------------------

	@Test
	void deleteEpic_shouldDeleteEpic() {
		Epic epic = new Epic();

		when(epicRepo.findById(1L)).thenReturn(Optional.of(epic));

		epicService.deleteEpic(1L);

		verify(epicRepo).delete(epic);
	}

	// ----------------------------
	// addUserStoryToEpic
	// ----------------------------

	@Test
	void addUserStoryToEpic_shouldAddUserStory() {
		Epic epic = new Epic();
		UserStory userStory = new UserStory();

		when(epicRepo.findById(1L)).thenReturn(Optional.of(epic));
		when(userStoryService.getUserStoryById(2L)).thenReturn(userStory);

		epicService.addUserStoryToEpic(1L, 2L);

		assertThat(epic.getUserStories()).contains(userStory);
	}

	// ----------------------------
	// removeUserStoryFromEpic
	// ----------------------------

	@Test
	void removeUserStoryFromEpic_shouldRemoveUserStory() {
		Epic epic = new Epic();
		UserStory userStory = new UserStory();
		epic.addUserStory(userStory);

		when(epicRepo.findById(1L)).thenReturn(Optional.of(epic));
		when(userStoryService.getUserStoryById(2L)).thenReturn(userStory);

		epicService.removeUserStoryFromEpic(1L, 2L);

		assertThat(epic.getUserStories()).doesNotContain(userStory);
	}

	// ----------------------------
	// getUserStoriesFromEpic
	// ----------------------------

	@Test
	void getUserStoriesFromEpic_shouldReturnUserStories() {
		Epic epic = new Epic();
		UserStory us1 = new UserStory();
		UserStory us2 = new UserStory();
		epic.addUserStory(us1);
		epic.addUserStory(us2);

		when(epicRepo.findById(1L)).thenReturn(Optional.of(epic));

		List<UserStory> result = epicService.getUserStoriesFromEpic(1L);

		assertThat(result).containsExactly(us1, us2);
	}

	// ----------------------------
	// vizualizeUserStoriesFromEpic
	// ----------------------------

	@Test
	void vizualizeUserStoriesFromEpic_shouldReturnTitlesSeparatedByNewLine() {
		Epic epic = new Epic();

		UserStory us1 = new UserStory();
		us1.setTitle("Login");

		UserStory us2 = new UserStory();
		us2.setTitle("Register");

		epic.addUserStory(us1);
		epic.addUserStory(us2);

		when(epicRepo.findById(1L)).thenReturn(Optional.of(epic));

		String result = epicService.vizualizeUserStoriesFromEpic(1L);

		assertThat(result)
				.isEqualTo("Login\nRegister\n");
	}
}
