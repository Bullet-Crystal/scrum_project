package org.example.backlog;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserStoryRepository extends JpaRepository<UserStory, Long> {

    List<UserStory> findByProductBacklogIdOrderByIdAsc(Long productBacklogId);

    List<UserStory> findByEpicIdOrderByIdAsc(Long epicId);

    List<UserStory> findBySprintBacklogIdOrderByIdAsc(Long sprintBacklogId);

    boolean existsByIdAndProductBacklogId(Long id, Long productBacklogId);

    boolean existsByProductBacklogIdAndTitleIgnoreCase(Long productBacklogId, String title);
}
