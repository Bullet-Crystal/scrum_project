package org.example.service;

import org.example.repository.UseCaseRepository;
import org.example.model.Statut;

import org.example.model.UserStory;
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
    public void addUserStory(UserStory userStory) {
        repository.save(userStory);
    }



    @Override
    public void updateUserStory(UserStory oldstory, UserStory newstory) {
            UserStory story = repository.findById(oldstory.getId())
                            .orElseThrow(() -> new IllegalArgumentException("UserStory not found"));
            story.setTitle(newstory.getTitle());
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
    public void updateUserStoryStatus(UserStory u, Statut s) {
        UserStory userStory = repository.findById(u.getId())
                .orElseThrow(() -> new IllegalArgumentException("User Story not found"));
        userStory.setUserStoryStatut(s);
        repository.save(userStory);
    }



    @Override
    public void deleteUserStory(UserStory u) {
        if(repository.existsById(u.getId())) {
            repository.delete(u);
        }

    }

    @Override
    public Boolean existsByTitle(String title) {
        return repository.existsUserStoryByTitle(title);
    }
}
