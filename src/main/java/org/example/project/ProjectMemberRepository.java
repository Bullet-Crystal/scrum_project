package org.example.project;

import java.util.List;
import java.util.Optional;

import org.example.user.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {

    Optional<ProjectMember> findByProjectIdAndUserUsername(Long projectId, String username);

    Optional<ProjectMember> findByProjectIdAndUserId(Long projectId, Long userId);

    List<ProjectMember> findByProjectIdOrderByIdAsc(Long projectId);

    long countByProjectIdAndRole(Long projectId, RoleType role);
}
