package Service;

import JPA.SprintBacklogRepository;
import Model.SprintBacklog;
import Model.Task;
import Model.UserStory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor

public class SprintBacklogServiceImp implements SprintBacklogService {

    private  SprintBacklogRepository sprintBacklogRepository;
    private  UserStoryService userStoryService;
    private final ProductBacklogService productBacklogService ;


    public SprintBacklogServiceImp(
            SprintBacklogRepository sprintBacklogRepository,
            UserStoryService userStoryService) {
        this.sprintBacklogRepository = sprintBacklogRepository;
        this.userStoryService = userStoryService;
    }


    @Override
    public void addSprintUserStories(Long sprintId,Long productBacklogId,UserStory userStory) {
        ProductBacklog productBacklog = productBacklogService.getProductBacklogById(productBacklogId);
        if(productBacklog.containsUserStory(userStory)){
            SprintBacklog sprintBacklog = sprintBacklogRepository.findById(sprintId)
                    .orElseThrow(()->new IllegalArgumentException("Sprint backlog not found") );
            sprintBacklog.addUserStory(userStory);
            sprintBacklogRepository.save(sprintBacklog);
        }
    }


    @Override
    public void removeSprintUserStories(Long sprintId,UserStory userStory) {
        if(!userStoryService.isAccepted(userStory)){return ;}
        SprintBacklog sprintBacklog = sprintBacklogRepository.findById(sprintId)
                .orElseThrow(()->new IllegalArgumentException("Sprint backlog not found") );
        sprintBacklog.removeUserStory(userStory);
        sprintBacklogRepository.save(sprintBacklog);
    }


    @Override
    public void addSprintTask(Long sprintId,Long userStoryId,Task task) {
        SprintBacklog sprintBacklog = sprintBacklogRepository.findSprintBacklogById(sprintId);
        UserStory userStory = userStoryService.getUserStoryById(userStoryId);
        if(!userStory.containsTask(task)){
            userStory.addTask(task);
            sprintBacklog.addTask(task);
            userStoryService.saveUserStory(userStory);
            sprintBacklogRepository.save(sprintBacklog);
        }
    }

    @Override
    public void removeSprintTask(Long sprintId,Task task) {
        SprintBacklog sprintBacklog = sprintBacklogRepository.findById(sprintId)
                        .orElseThrow(()->new IllegalArgumentException("Sprint backlog not found") );
        sprintBacklog.removeTask(task);

        sprintBacklogRepository.save(sprintBacklog);
    }

}
