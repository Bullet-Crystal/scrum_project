package org.example.backlog;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EpicRepository extends JpaRepository<Epic, Long> {

    List<Epic> findByProductBacklogIdOrderByIdAsc(Long productBacklogId);
}
