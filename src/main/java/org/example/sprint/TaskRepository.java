package org.example.sprint;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findBySprintBacklogIdOrderByIdAsc(Long sprintBacklogId);

    List<Task> findByUserStoryIdOrderByIdAsc(Long userStoryId);
}
