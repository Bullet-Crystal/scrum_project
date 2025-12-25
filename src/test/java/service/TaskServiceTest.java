package service;


import org.example.repository.TaskRepository;
import org.example.model.Statut;
import org.example.model.Task;
import org.example.service.TaskServiceImp;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {
    @Mock
    private TaskRepository repository;
    @InjectMocks
    private TaskServiceImp service;

    @Test

    void modifierTask_shouldUpdateFields_whenTaskIdExists() {
        Task oldTask = new Task();
        oldTask.setId(1L);
        Task newTask = new Task();
        newTask.setId(2L);
        newTask.setTitre("test");
        newTask.setDescription("test");


        when(repository.findById(1L)).thenReturn(Optional.of(oldTask));

        service.modifierTask(oldTask, newTask);

        assertEquals(newTask.getId(), oldTask.getId());
        assertEquals(newTask.getTitre(), oldTask.getTitre());
        assertEquals(newTask.getDescription(), oldTask.getDescription());
        verify(repository).save(oldTask);
    }

    @Test
    void modifierTask_shouldThrowException_whenTaskIdDoesNotExist() {
        Task oldTask = new Task();
        oldTask.setId(1L);

        Task newTask = new Task();

        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> service.modifierTask(oldTask, newTask));
    }

    @Test
    void modifierTaskStatut_shouldUpdateFields_whenTaskIdExists() {
        Task oldTask = new Task();
        oldTask.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(oldTask));
        service.modifierTaskStatus(oldTask, Statut.DONE);
        assertEquals(Statut.DONE,oldTask.getTaskStatut());
        verify(repository).save(oldTask);
    }

    @Test
    void modifierTaskStatut_shouldThrowException_whenTaskIdDoesNotExist() {
        Task oldTask = new Task();
        oldTask.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> service.modifierTaskStatus(oldTask, Statut.DONE));
    }

}
