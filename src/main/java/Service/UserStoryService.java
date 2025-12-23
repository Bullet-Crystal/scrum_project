package Service;


import Model.Statut;

import Model.UserStory;
import org.springframework.stereotype.Service;


@Service

public interface UserStoryService {

    UserStory getUserStoryById(Long id);
    void modifierUserStory(UserStory u, UserStory u2);
    Boolean isAccepted(UserStory u);
    void modifierUserStoryStatus(UserStory u, Statut s);
    void saveUserStory(UserStory u);
}
