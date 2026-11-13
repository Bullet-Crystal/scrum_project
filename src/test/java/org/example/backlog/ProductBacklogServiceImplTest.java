package org.example.backlog;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.example.backlog.dto.CreateUserStoryRequest;
import org.example.backlog.dto.UserStoryResponse;
import org.example.common.ConflictException;
import org.example.project.Project;
import org.example.project.ProjectAccessService;
import org.example.sprint.Status;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductBacklogServiceImplTest {

    @Mock
    private ProductBacklogRepository backlogRepository;
    @Mock
    private UserStoryRepository userStoryRepository;
    @Mock
    private ProjectAccessService accessService;
    @Spy
    private BacklogMapper backlogMapper = new BacklogMapper();

    @InjectMocks
    private ProductBacklogServiceImpl service;

    private ProductBacklog backlog() {
        Project project = Project.builder().id(5L).title("P").build();
        ProductBacklog backlog = ProductBacklog.builder().id(10L).title("Backlog").project(project).build();
        project.setProductBacklog(backlog);
        return backlog;
    }

    @Test
    void addUserStory_setsOwningSideAndSaves() {
        ProductBacklog backlog = backlog();
        when(accessService.requireOwnerForBacklog(10L, "alice")).thenReturn(null);
        when(backlogRepository.findById(10L)).thenReturn(Optional.of(backlog));
        when(userStoryRepository.existsByProductBacklogIdAndTitleIgnoreCase(10L, "Login")).thenReturn(false);
        when(userStoryRepository.save(any(UserStory.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserStoryResponse response = service.addUserStory(
                10L,
                new CreateUserStoryRequest("Login", "user", "log in", "access data",
                        Priority.MUST_HAVE, null, "criteria"),
                "alice");

        assertThat(response.title()).isEqualTo("Login");
        assertThat(response.status()).isEqualTo(Status.TO_DO);
        assertThat(response.productBacklogId()).isEqualTo(10L);
    }

    @Test
    void addUserStory_rejectsDuplicateTitleInSameBacklog() {
        when(accessService.requireOwnerForBacklog(10L, "alice")).thenReturn(null);
        when(backlogRepository.findById(10L)).thenReturn(Optional.of(backlog()));
        when(userStoryRepository.existsByProductBacklogIdAndTitleIgnoreCase(10L, "Login")).thenReturn(true);

        assertThatThrownBy(() -> service.addUserStory(
                10L,
                new CreateUserStoryRequest("Login", "user", "log in", "access data",
                        Priority.MUST_HAVE, null, null),
                "alice"))
                .isInstanceOf(ConflictException.class);
    }
}
