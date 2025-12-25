package org.example.repository;

import org.example.model.UserStory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UseCaseRepository extends JpaRepository<UserStory,Long> {

    boolean existsUserStoryByTitle(String title);


}
