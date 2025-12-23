package Service;

import JPA.TaskRepository;
import Model.Statut;
import Model.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskServiceImp implements TaskService {
    private final TaskRepository taskRepository;

    @Override
    public void modifierTask(Task oldtask,Task newtask) {
        Task task2 = taskRepository.findById(oldtask.getId())
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));
        task2.setId(newtask.getId());
        task2.setTitre(newtask.getTitre());
        task2.setDescription(newtask.getDescription());
        taskRepository.save(task2);
    }

    @Override
    public void modifierTaskStatus(Task task, Statut status) {
        Task task1 = taskRepository.findById(task.getId())
                .orElseThrow(()-> new IllegalArgumentException("Task not found"));
        task1.setTaskStatut(status);
        taskRepository.save(task1);

    }

}
