package org.example.repository;

import org.example.model.SprintBacklog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SprintBacklogRepository extends JpaRepository<SprintBacklog, Long> {
    SprintBacklog findSprintBacklogById(Long id);
}
