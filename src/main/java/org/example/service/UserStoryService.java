package org.example.service;


import org.example.model.Statut;

import org.example.model.UserStory;
import org.springframework.stereotype.Service;


@Service

public interface UserStoryService {

    UserStory getUserStoryById(Long id);
    void addUserStory(UserStory userStory);
    void updateUserStory(UserStory u, UserStory u2);
    Boolean isAccepted(UserStory u);
    void updateUserStoryStatus(UserStory u, Statut s);
    void deleteUserStory(UserStory u);
    Boolean existsByTitle(String title);
}
