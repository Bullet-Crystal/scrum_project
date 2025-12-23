package org.example.repository;

import java.util.List;

import org.example.model.Epic;
import org.example.model.UserStory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserStoryRepository extends JpaRepository<UserStory, Long> {
	List<UserStory> findByEpic(Epic epic);

	Boolean existsUserStoryByTitle(String Title);
}
