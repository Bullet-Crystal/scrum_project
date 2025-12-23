package Service;


import Model.Task;
import Model.UserStory;



public interface SprintBacklogService {

    void addSprintUserStories(Long sprintId,Long productBacklogId,UserStory userStory);
    void removeSprintUserStories(Long SprintId,UserStory userStory);
    void removeSprintTask(Long sprintId,Task task);
    void addSprintTask(Long sprintId,Long userStoryId,Task task);

}
