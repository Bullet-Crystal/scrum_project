package org.example.backlog;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductBacklogRepository extends JpaRepository<ProductBacklog, Long> {

    Optional<ProductBacklog> findByProjectId(Long projectId);
}
