package JPA;

import Model.UserStory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UseCaseRepository extends JpaRepository<UserStory,Long> {

    boolean existsUserStoryByTitre(String titre);
    UserStory findUserStoryByTitre(String titre);

}
