package org.example.project;

import java.util.List;

import org.example.backlog.ProductBacklog;
import org.example.common.BadRequestException;
import org.example.common.ConflictException;
import org.example.common.NotFoundException;
import org.example.project.dto.AddProjectMemberRequest;
import org.example.project.dto.CreateProjectRequest;
import org.example.project.dto.ProjectMemberResponse;
import org.example.project.dto.ProjectResponse;
import org.example.project.dto.UpdateProjectRequest;
import org.example.user.RoleType;
import org.example.user.User;
import org.example.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository memberRepository;
    private final UserRepository userRepository;
    private final ProjectAccessService accessService;
    private final ProjectMapper projectMapper;

    @Override
    public ProjectResponse create(CreateProjectRequest request, String username) {
        User owner = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found: " + username));

        Project project = Project.builder()
                .title(request.title())
                .description(request.description())
                .build();
        project.addMember(owner, RoleType.PRODUCT_OWNER);

        ProductBacklog backlog = ProductBacklog.builder()
                .title("Backlog for " + request.title())
                .build();
        project.setProductBacklog(backlog);

        return projectMapper.toResponse(projectRepository.save(project));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectResponse> getForUser(String username) {
        return projectRepository.findAllForUsername(username).stream()
                .map(projectMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectResponse get(Long projectId, String username) {
        accessService.requireMember(projectId, username);
        return projectMapper.toResponse(getEntity(projectId));
    }

    @Override
    public ProjectResponse update(Long projectId, UpdateProjectRequest request, String username) {
        accessService.requireOwner(projectId, username);
        Project project = getEntity(projectId);
        project.setTitle(request.title());
        project.setDescription(request.description());
        return projectMapper.toResponse(project);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectMemberResponse> getMembers(Long projectId, String username) {
        accessService.requireMember(projectId, username);
        return memberRepository.findByProjectIdOrderByIdAsc(projectId).stream()
                .map(projectMapper::toMemberResponse)
                .toList();
    }

    @Override
    public ProjectMemberResponse addMember(Long projectId, AddProjectMemberRequest request, String username) {
        accessService.requireOwner(projectId, username);
        Project project = getEntity(projectId);

        if (memberRepository.findByProjectIdAndUserUsername(projectId, request.username()).isPresent()) {
            throw new ConflictException("User is already a member of this project");
        }

        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new NotFoundException("User not found: " + request.username()));

        ProjectMember member = project.addMember(user, request.role());
        return projectMapper.toMemberResponse(memberRepository.save(member));
    }

    @Override
    public void removeMember(Long projectId, Long userId, String username) {
        accessService.requireOwner(projectId, username);
        Project project = getEntity(projectId);
        ProjectMember member = memberRepository.findByProjectIdAndUserId(projectId, userId)
                .orElseThrow(() -> new NotFoundException("Project member not found"));

        if (member.getRole() == RoleType.PRODUCT_OWNER
                && memberRepository.countByProjectIdAndRole(projectId, RoleType.PRODUCT_OWNER) <= 1) {
            throw new BadRequestException("A project must keep at least one product owner");
        }

        project.removeMember(member);
        memberRepository.delete(member);
    }

    @Override
    @Transactional(readOnly = true)
    public Project getEntity(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException("Project not found: " + projectId));
    }
}
