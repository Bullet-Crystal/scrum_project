
package org.example.controller;

import java.util.Set;

import org.example.dto.TaskModificationDTO;
import org.example.dto.TaskStatusDTO;
import org.example.mapper.TaskMapper;
import org.example.model.RoleType;
import org.example.model.Task;
import org.example.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import org.example.security.JwtUtil;
import org.example.service.ProjectService;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

	private final TaskService taskService;
	private final TaskMapper mapper;
	private final ProjectService projectService;
	private final JwtUtil jwtUtil;

	@PutMapping("/{id}")
	public ResponseEntity<String> modifierTask(
			@PathVariable Long id,
			@RequestBody TaskModificationDTO dto,
			@RequestHeader("Authorization") String authHeader) {

		String token = authHeader.replace("Bearer ", "");
		String username = jwtUtil.extractUsername(token);
		Set<RoleType> roles = jwtUtil.extractRoles(token);
		Set<RoleType> authorizedRoles = Set.of(
				RoleType.PRODUCT_OWNER,
				RoleType.DEVELOPER);

		taskService.isAuthorizedInTask(id, username);
		projectService.assertAuthorized(roles, authorizedRoles);

		Task taskExistante = taskService.getTaskById(id);

		Task taskModifiee = mapper.updateTaskFromDTO(taskExistante, dto);

		taskService.modifierTask(taskExistante, taskModifiee);

		return ResponseEntity.ok("Task modifiée avec succès");
	}

	@PutMapping("/{id}/status")
	public ResponseEntity<String> modifierTaskStatus(
			@PathVariable Long id,
			@RequestBody TaskStatusDTO dto,
			@RequestHeader("Authorization") String authHeader) {

		String token = authHeader.replace("Bearer ", "");
		String username = jwtUtil.extractUsername(token);
		Set<RoleType> roles = jwtUtil.extractRoles(token);
		Set<RoleType> authorizedRoles = Set.of(
				RoleType.PRODUCT_OWNER,
				RoleType.DEVELOPER);

		taskService.isAuthorizedInTask(id, username);
		projectService.assertAuthorized(roles, authorizedRoles);

		Task task = taskService.getTaskById(id);
		taskService.modifierTaskStatus(task, dto.getStatus());

		return ResponseEntity.ok("Statut de la task modifié avec succès");
	}

}
