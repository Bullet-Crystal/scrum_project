package Service;

import Model.Statut;
import Model.Task;

public interface TaskService {

     void modifierTask(Task task,Task task1);
     void modifierTaskStatus(Task task, Statut statut);

}
