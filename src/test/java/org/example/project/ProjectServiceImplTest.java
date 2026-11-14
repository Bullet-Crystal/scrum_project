package org.example.project;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.example.common.BadRequestException;
import org.example.project.dto.CreateProjectRequest;
import org.example.project.dto.ProjectResponse;
import org.example.project.dto.UpdateProjectRequest;
import org.example.user.RoleType;
import org.example.user.User;
import org.example.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProjectServiceImplTest {

    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private ProjectMemberRepository memberRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ProjectAccessService accessService;
    @Spy
    private ProjectMapper projectMapper = new ProjectMapper();

    @InjectMocks
    private ProjectServiceImpl projectService;

    @Test
    void create_addsOwnerAndCreatesProductBacklog() {
        User owner = User.builder().id(1L).username("alice").roles(java.util.Set.of(RoleType.DEVELOPER)).build();
        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(owner));
        when(projectRepository.save(any(Project.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProjectResponse response = projectService.create(new CreateProjectRequest("Scrum", "desc"), "alice");

        assertThat(response.title()).isEqualTo("Scrum");
        assertThat(response.productBacklog()).isNotNull();
        assertThat(response.productBacklog().title()).isEqualTo("Backlog for Scrum");

        ArgumentCaptor<Project> captor = ArgumentCaptor.forClass(Project.class);
        verify(projectRepository).save(captor.capture());
        Project saved = captor.getValue();
        assertThat(saved.getMembers()).hasSize(1);
        assertThat(saved.getMembers().get(0).getRole()).isEqualTo(RoleType.PRODUCT_OWNER);
        assertThat(saved.getProductBacklog().getProject()).isSameAs(saved);
    }

    @Test
    void update_requiresOwner() {
        Project project = Project.builder().id(1L).title("Old").build();
        when(accessService.requireOwner(1L, "alice")).thenReturn(new ProjectMember());
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        ProjectResponse response = projectService.update(1L, new UpdateProjectRequest("New", "d"), "alice");

        assertThat(response.title()).isEqualTo("New");
        verify(accessService).requireOwner(1L, "alice");
    }

    @Test
    void removeMember_preventsRemovingLastOwner() {
        Project project = Project.builder().id(1L).build();
        ProjectMember member = ProjectMember.builder()
                .id(2L)
                .project(project)
                .user(User.builder().id(3L).username("bob").build())
                .role(RoleType.PRODUCT_OWNER)
                .build();
        when(accessService.requireOwner(1L, "alice")).thenReturn(new ProjectMember());
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(memberRepository.findByProjectIdAndUserId(1L, 3L)).thenReturn(Optional.of(member));
        when(memberRepository.countByProjectIdAndRole(1L, RoleType.PRODUCT_OWNER)).thenReturn(1L);

        assertThatThrownBy(() -> projectService.removeMember(1L, 3L, "alice"))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    void addMember_rejectsExistingMember() {
        when(accessService.requireOwner(1L, "alice")).thenReturn(new ProjectMember());
        when(projectRepository.findById(1L)).thenReturn(Optional.of(Project.builder().id(1L).build()));
        when(memberRepository.findByProjectIdAndUserUsername(1L, "bob")).thenReturn(Optional.of(new ProjectMember()));

        assertThatThrownBy(() -> projectService.addMember(
                1L, new org.example.project.dto.AddProjectMemberRequest("bob", RoleType.DEVELOPER), "alice"))
                .isInstanceOf(org.example.common.ConflictException.class);
    }
}
