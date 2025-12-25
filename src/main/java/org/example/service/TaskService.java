package org.example.service;

import org.example.model.Statut;
import org.example.model.Task;

public interface TaskService {

     void modifierTask(Task task,Task task1);
     void modifierTaskStatus(Task task, Statut statut);

}
