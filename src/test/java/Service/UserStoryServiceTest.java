package Service;


import JPA.UseCaseRepository;
import Model.Priority;
import Model.UserStory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserStoryServiceTest {
    @Mock
    private UseCaseRepository repository ;

    @InjectMocks
    private UserStoryImp userStoryService ;

    @Test
    void shouldReturnUserStory_whenIdExists() {

        UserStory story = new UserStory();
        story.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(story));

        UserStory result = userStoryService.getUserStoryById(1L);

        assertNotNull(result);
        assertEquals(1L,result.getId());
    }


    @Test
    void shouldReturnUserStory_whenIdDoesNotExist() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> userStoryService.getUserStoryById(1L));
    }

    @Test
    void modifierUserStory_shouldUpdateFields_whenUserStoryExists() {
        UserStory oldStory = new UserStory();
        oldStory.setId(1L);

        UserStory newStory = new UserStory();
        newStory.setTitre("Nouveau titre");
        newStory.setPriority(Priority.SHOULD_HAVE);

        when(repository.findById(1L)).thenReturn(Optional.of(oldStory));

        userStoryService.modifierUserStory(oldStory, newStory);

        assertEquals("Nouveau titre", oldStory.getTitre());
        assertEquals(Priority.SHOULD_HAVE, oldStory.getPriority());
        verify(repository).save(oldStory);
    }

    @Test
    void modifierUserStory_shouldThrowException_whenUserStoryDoesNotExist() {
        UserStory oldStory = new UserStory();
        oldStory.setId(1L);

        UserStory newStory = new UserStory();

        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> userStoryService.modifierUserStory(oldStory, newStory));
    }

    @Test
    void saveUserStory_shouldCallRepositorySave() {
        UserStory us = new UserStory();

        userStoryService.saveUserStory(us);

        verify(repository).save(us);
    }


    @Test
    void isAccepted_shouldReturnNull_forNow() {
        UserStory us = new UserStory();

        Boolean result = userStoryService.isAccepted(us);

        assertNull(result);
    }

}
