package org.example.backlog;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;

import org.example.backlog.dto.CreateEpicRequest;
import org.example.backlog.dto.EpicResponse;
import org.example.common.BadRequestException;
import org.example.project.Project;
import org.example.project.ProjectAccessService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EpicServiceImplTest {

    @Mock
    private EpicRepository epicRepository;
    @Mock
    private UserStoryRepository userStoryRepository;
    @Mock
    private ProductBacklogService productBacklogService;
    @Mock
    private ProjectAccessService accessService;
    @Spy
    private BacklogMapper backlogMapper = new BacklogMapper();

    @InjectMocks
    private EpicServiceImpl service;

    private ProductBacklog backlog(long id) {
        Project project = Project.builder().id(1L).title("P").build();
        ProductBacklog backlog = ProductBacklog.builder().id(id).title("B").project(project).build();
        project.setProductBacklog(backlog);
        return backlog;
    }

    @Test
    void create_attachesEpicToBacklog() {
        ProductBacklog backlog = backlog(10L);
        when(accessService.requireOwnerForBacklog(10L, "alice")).thenReturn(null);
        when(productBacklogService.getEntity(10L)).thenReturn(backlog);
        when(epicRepository.save(any(Epic.class))).thenAnswer(invocation -> invocation.getArgument(0));

        EpicResponse response = service.create(10L, new CreateEpicRequest("Epic", "d", "g", null), "alice");

        assertThat(response.title()).isEqualTo("Epic");
        assertThat(response.productBacklogId()).isEqualTo(10L);
        assertThat(backlog.getEpics()).hasSize(1);
    }

    @Test
    void addUserStory_rejectsStoryFromAnotherBacklog() {
        ProductBacklog backlog = backlog(10L);
        Epic epic = Epic.builder().id(20L).title("E").productBacklog(backlog).userStories(new ArrayList<>()).build();
        UserStory story = UserStory.builder().id(30L).productBacklog(backlog(99L)).build();

        when(accessService.requireOwnerForEpic(20L, "alice")).thenReturn(null);
        when(accessService.getEpic(20L)).thenReturn(epic);
        when(accessService.getStory(30L)).thenReturn(story);

        assertThatThrownBy(() -> service.addUserStory(20L, 30L, "alice"))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    void addUserStory_setsBothSides() {
        ProductBacklog backlog = backlog(10L);
        Epic epic = Epic.builder().id(20L).title("E").productBacklog(backlog).userStories(new ArrayList<>()).build();
        UserStory story = UserStory.builder().id(30L).productBacklog(backlog).build();

        when(accessService.requireOwnerForEpic(20L, "alice")).thenReturn(null);
        when(accessService.getEpic(20L)).thenReturn(epic);
        when(accessService.getStory(30L)).thenReturn(story);

        service.addUserStory(20L, 30L, "alice");

        assertThat(story.getEpic()).isSameAs(epic);
        assertThat(epic.getUserStories()).containsExactly(story);
    }

    @Test
    void delete_detachesStoriesInsteadOfDeletingThem() {
        ProductBacklog backlog = backlog(10L);
        Epic epic = Epic.builder().id(20L).title("E").productBacklog(backlog).userStories(new ArrayList<>()).build();
        UserStory story = UserStory.builder().id(30L).productBacklog(backlog).build();
        epic.addUserStory(story);

        when(accessService.requireOwnerForEpic(20L, "alice")).thenReturn(null);
        when(accessService.getEpic(20L)).thenReturn(epic);

        service.delete(20L, "alice");

        assertThat(story.getEpic()).isNull();
        assertThat(epic.getUserStories()).isEmpty();
        verify(epicRepository).delete(epic);
    }
}
