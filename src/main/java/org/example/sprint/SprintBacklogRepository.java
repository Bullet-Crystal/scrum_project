package org.example.sprint;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SprintBacklogRepository extends JpaRepository<SprintBacklog, Long> {

    List<SprintBacklog> findByProjectIdOrderByStartDateAsc(Long projectId);

    boolean existsByProjectIdAndNameIgnoreCase(Long projectId, String name);
}
