
package org.example.controller;

import java.util.Set;

import org.example.dto.AddTaskToSprintDTO;
import org.example.dto.AddUserStoryToSprintDTO;
import org.example.dto.RemoveTaskDTO;
import org.example.dto.RemoveUserStoryDTO;
import org.example.service.SprintBacklogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.example.security.JwtUtil;
import org.example.service.ProjectService;
import lombok.RequiredArgsConstructor;

import org.example.mapper.SprintBacklogMapper;
import org.example.model.RoleType;

@RestController
@RequestMapping("/api/sprint-backlogs")
@RequiredArgsConstructor
public class SprintBacklogController {

	private final SprintBacklogService sprintBacklogService;
	private SprintBacklogMapper mapper;
	private final ProjectService projectService;
	private final JwtUtil jwtUtil;

	@PostMapping("/{sprintId}/add-user-story")
	public ResponseEntity<String> addUserStoryToSprint(
			@PathVariable Long sprintId,
			@RequestBody AddUserStoryToSprintDTO dto,
			@RequestHeader("Authorization") String authHeader) {

		String token = authHeader.replace("Bearer ", "");
		String username = jwtUtil.extractUsername(token);
		Set<RoleType> roles = jwtUtil.extractRoles(token);
		Set<RoleType> authorizedRoles = Set.of(
				RoleType.PRODUCT_OWNER);

		sprintBacklogService.isAuthorizedInSprint(sprintId, username);
		projectService.assertAuthorized(roles, authorizedRoles);

		sprintBacklogService.addSprintUserStories(
				sprintId,
				dto.getProductBacklogId(),
				mapper.convertToUserStory(dto));
		return ResponseEntity.status(HttpStatus.CREATED).body("User Story ajoutée au sprint avec succès");
	}

	@DeleteMapping("/{sprintId}/remove-user-story")
	public ResponseEntity<String> removeUserStoryFromSprint(
			@PathVariable Long sprintId,
			@RequestBody RemoveUserStoryDTO dto,
			@RequestHeader("Authorization") String authHeader) {
		String token = authHeader.replace("Bearer ", "");
		String username = jwtUtil.extractUsername(token);
		Set<RoleType> roles = jwtUtil.extractRoles(token);
		Set<RoleType> authorizedRoles = Set.of(
				RoleType.PRODUCT_OWNER);

		sprintBacklogService.isAuthorizedInSprint(sprintId, username);
		projectService.assertAuthorized(roles, authorizedRoles);

		sprintBacklogService.removeSprintUserStories(
				sprintId,
				mapper.convertToUserStory(dto));
		return ResponseEntity.ok("User Story retirée du sprint avec succès");
	}

	@PostMapping("/{sprintId}/add-task")
	public ResponseEntity<String> addTaskToSprint(
			@PathVariable Long sprintId,
			@RequestBody AddTaskToSprintDTO dto,
			@RequestHeader("Authorization") String authHeader) {

		String token = authHeader.replace("Bearer ", "");
		String username = jwtUtil.extractUsername(token);
		Set<RoleType> roles = jwtUtil.extractRoles(token);
		Set<RoleType> authorizedRoles = Set.of(
				RoleType.PRODUCT_OWNER,
				RoleType.DEVELOPER);

		sprintBacklogService.isAuthorizedInSprint(sprintId, username);
		projectService.assertAuthorized(roles, authorizedRoles);

		sprintBacklogService.addSprintTask(
				sprintId,
				dto.getUserStoryId(),
				mapper.convertToTask(dto));

		return ResponseEntity.status(HttpStatus.CREATED)
				.body("Task ajoutée au sprint avec succès");
	}

	@DeleteMapping("/{sprintId}/remove-task")
	public ResponseEntity<String> removeTaskFromSprint(
			@PathVariable Long sprintId,
			@RequestBody RemoveTaskDTO dto,
			@RequestHeader("Authorization") String authHeader) {

		String token = authHeader.replace("Bearer ", "");
		String username = jwtUtil.extractUsername(token);
		Set<RoleType> roles = jwtUtil.extractRoles(token);
		Set<RoleType> authorizedRoles = Set.of(
				RoleType.PRODUCT_OWNER,
				RoleType.DEVELOPER);

		sprintBacklogService.isAuthorizedInSprint(sprintId, username);
		projectService.assertAuthorized(roles, authorizedRoles);

		sprintBacklogService.removeSprintTask(
				sprintId,
				mapper.convertToTask(dto));
		return ResponseEntity.ok("Task retirée du sprint avec succès");
	}

}
