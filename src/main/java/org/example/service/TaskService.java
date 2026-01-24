package org.example.service;

import org.example.model.Status;
import org.example.model.Task;

public interface TaskService {

	void modifierTask(Task task, Task task1);

	Task getTaskById(Long id);

	void isAuthorizedInTask(Long id, String username);

	void modifierTaskStatus(Task task, Status statut);

}
