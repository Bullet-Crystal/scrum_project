package org.example.project;

import java.util.List;

import org.example.project.dto.AddProjectMemberRequest;
import org.example.project.dto.CreateProjectRequest;
import org.example.project.dto.ProjectMemberResponse;
import org.example.project.dto.ProjectResponse;
import org.example.project.dto.UpdateProjectRequest;

public interface ProjectService {

    ProjectResponse create(CreateProjectRequest request, String username);

    List<ProjectResponse> getForUser(String username);

    ProjectResponse get(Long projectId, String username);

    ProjectResponse update(Long projectId, UpdateProjectRequest request, String username);

    List<ProjectMemberResponse> getMembers(Long projectId, String username);

    ProjectMemberResponse addMember(Long projectId, AddProjectMemberRequest request, String username);

    void removeMember(Long projectId, Long userId, String username);

    Project getEntity(Long projectId);
}
