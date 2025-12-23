package Service;

import JPA.UseCaseRepository;
import Model.Statut;

import Model.UserStory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class UserStoryImp implements UserStoryService {
    private final UseCaseRepository repository;


    @Override
    public UserStory getUserStoryById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("UserStory not found"));
    }

    @Override
    public void modifierUserStory(UserStory oldstory, UserStory newstory) {
            UserStory story = repository.findById(oldstory.getId())
                            .orElseThrow(() -> new IllegalArgumentException("UserStory not found"));
            story.setTitre(newstory.getTitre());
            story.setDescription(newstory.getRole(),newstory.getAction(),newstory.getGoal());
            story.setPriority(newstory.getPriority());
            story.setUserStoryStatut(newstory.getUserStoryStatut());
            story.setCritereAcceptation(newstory.getCritereAcceptation());
            repository.save(story);
    }


    @Override
    public Boolean isAccepted(UserStory u) {
        return null;
    }

    @Override
    public void modifierUserStoryStatus(UserStory u, Statut s) {
        UserStory userStory = repository.findById(u.getId())
                .orElseThrow(() -> new IllegalStateException("User Story not found"));
        userStory.setUserStoryStatut(s);
        repository.save(userStory);
    }

    @Override
    public void saveUserStory(UserStory u) {
        repository.save(u);
    }
}
