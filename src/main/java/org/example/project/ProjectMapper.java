package org.example.project;

import org.example.backlog.dto.ProductBacklogResponse;
import org.example.project.dto.ProjectMemberResponse;
import org.example.project.dto.ProjectResponse;
import org.springframework.stereotype.Component;

@Component
public class ProjectMapper {

    public ProjectResponse toResponse(Project project) {
        ProductBacklogResponse backlog = project.getProductBacklog() == null
                ? null
                : new ProductBacklogResponse(
                        project.getProductBacklog().getId(),
                        project.getProductBacklog().getTitle(),
                        project.getId());

        return new ProjectResponse(
                project.getId(),
                project.getTitle(),
                project.getDescription(),
                project.getCreationDate(),
                backlog);
    }

    public ProjectMemberResponse toMemberResponse(ProjectMember member) {
        return new ProjectMemberResponse(
                member.getId(),
                member.getUser().getId(),
                member.getUser().getUsername(),
                member.getRole());
    }
}
