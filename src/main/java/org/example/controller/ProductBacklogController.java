package org.example.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.example.dto.PrioritizeUserStoryRequestDto;
import org.example.dto.ProductBacklogResponseDto;
import org.example.dto.UserStoryDto;
import org.example.dto.UserStoryResponseDto;
import org.example.mapper.ProductBacklogMapper;
import org.example.model.Priority;
import org.example.model.ProductBacklog;
import org.example.model.RoleType;
import org.example.model.Status;
import org.example.model.UserStory;
import org.example.security.JwtUtil;
import org.example.service.ProductBacklogService;
import org.example.service.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/product-backlogs")
@RequiredArgsConstructor
public class ProductBacklogController {
	private final ProductBacklogService productBacklogService;
	private final ProjectService projectService;
	private final ProductBacklogMapper productBacklogMapper;
	private final JwtUtil jwtUtil;

	/**
	 * Get product backlog by ID
	 */
	@GetMapping("/{backlogId}")
	public ResponseEntity<?> getProductBacklog(
			@PathVariable Long backlogId,
			@RequestHeader("Authorization") String authHeader) {
		try {
			String token = authHeader.replace("Bearer ", "");
			String username = jwtUtil.extractUsername(token);
			Set<RoleType> roles = jwtUtil.extractRoles(token);

			// Check if the user is authorized
			Set<RoleType> authorizedRoles = Set.of(
					RoleType.DEVELOPER,
					RoleType.SCRUM_MASTER,
					RoleType.PRODUCT_OWNER);
			projectService.assertAuthorized(roles, authorizedRoles);
			productBacklogService.isAuthorizedInBacklog(backlogId, username);

			ProductBacklog backlog = productBacklogService.getProductBacklogById(backlogId);
			ProductBacklogResponseDto response = productBacklogMapper.toProductBacklogResponse(backlog);

			return ResponseEntity.ok(response);

		} catch (EntityNotFoundException e) {
			return ResponseEntity
					.status(HttpStatus.NOT_FOUND)
					.body(Map.of("error", e.getMessage()));
		} catch (ResponseStatusException e) {
			return ResponseEntity
					.status(HttpStatus.FORBIDDEN)
					.body(Map.of("error", e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("error", "Failed to retrieve product backlog"));
		}
	}

	/**
	 * Add user story to product backlog
	 */
	@PostMapping("/{backlogId}/user-stories")
	public ResponseEntity<?> addUserStory(
			@PathVariable Long backlogId,
			@Valid @RequestBody UserStoryDto request,
			@RequestHeader("Authorization") String authHeader) {
		try {
			String token = authHeader.replace("Bearer ", "");
			String username = jwtUtil.extractUsername(token);
			Set<RoleType> roles = jwtUtil.extractRoles(token);

			Set<RoleType> authorizedRoles = Set.of(
					RoleType.PRODUCT_OWNER);
			projectService.assertAuthorized(roles, authorizedRoles);
			productBacklogService.isAuthorizedInBacklog(backlogId, username);

			// Create UserStory
			String title = request.getTitle();
			String role = request.getRole();
			String action = request.getAction();
			String goal = request.getGoal();
			Priority priority = request.getPriority();
			Status status = request.getUserStoryStatut();

			UserStory userStory = UserStory.builder()
					.role(role)
					.title(title)
					.action(action)
					.goal(goal)
					.priority(priority)
					.status(status)
					.build();

			productBacklogService.addUserStoryToBacklog(backlogId, userStory);

			return ResponseEntity
					.status(HttpStatus.CREATED)
					.body(Map.of("message", "User story added successfully"));

		} catch (EntityNotFoundException e) {
			return ResponseEntity
					.status(HttpStatus.NOT_FOUND)
					.body(Map.of("error", e.getMessage()));
		} catch (ResponseStatusException e) {
			return ResponseEntity
					.status(HttpStatus.FORBIDDEN)
					.body(Map.of("error", e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("error", "Failed to add user story"));
		}
	}

	/**
	 * Get all user stories from a product backlog
	 */
	@GetMapping("/{backlogId}/user-stories")
	public ResponseEntity<?> getAllUserStories(
			@PathVariable Long backlogId,
			@RequestHeader("Authorization") String authHeader) {
		try {
			String token = authHeader.replace("Bearer ", "");
			String username = jwtUtil.extractUsername(token);
			Set<RoleType> roles = jwtUtil.extractRoles(token);

			Set<RoleType> authorizedRoles = Set.of(
					RoleType.PRODUCT_OWNER);
			projectService.assertAuthorized(roles, authorizedRoles);

			productBacklogService.isAuthorizedInBacklog(backlogId, username);

			ProductBacklog backlog = productBacklogService.getProductBacklogById(backlogId);

			List<UserStoryResponseDto> response = backlog.getUserStories().stream()
					.map(userStory -> UserStoryResponseDto.builder()
							.id(userStory.getId())
							.title(userStory.getTitle())
							.role(userStory.getRole())
							.action(userStory.getAction())
							.goal(userStory.getGoal())
							.priority(userStory.getPriority())
							.status(userStory.getStatus())
							.build())
					.collect(Collectors.toList());

			return ResponseEntity.ok(response);

		} catch (EntityNotFoundException e) {
			return ResponseEntity
					.status(HttpStatus.NOT_FOUND)
					.body(Map.of("error", e.getMessage()));
		} catch (ResponseStatusException e) {
			return ResponseEntity
					.status(e.getStatusCode())
					.body(Map.of("error", e.getReason()));
		} catch (Exception e) {
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("error", "Failed to retrieve user stories"));
		}
	}

	/**
	 * Update user story in product backlog
	 */
	@PutMapping("/{backlogId}/user-stories/{userStoryId}")
	public ResponseEntity<?> updateUserStory(
			@PathVariable Long backlogId,
			@PathVariable Long userStoryId,
			@Valid @RequestBody UserStoryDto request,
			@RequestHeader("Authorization") String authHeader) {
		try {
			String token = authHeader.replace("Bearer ", "");
			String username = jwtUtil.extractUsername(token);
			Set<RoleType> roles = jwtUtil.extractRoles(token);

			Set<RoleType> authorizedRoles = Set.of(
					RoleType.PRODUCT_OWNER);
			projectService.assertAuthorized(roles, authorizedRoles);
			productBacklogService.isAuthorizedInBacklog(backlogId, username);

			// Create UserStory
			String title = request.getTitle();
			String role = request.getRole();
			String action = request.getAction();
			String goal = request.getGoal();
			Priority priority = request.getPriority();
			Status status = request.getUserStoryStatut();
			UserStory userStory = UserStory.builder()
					.title(title)
					.priority(priority)
					.status(status)
					.action(action)
					.goal(goal)
					.role(role)
					.build();

			productBacklogService.updateUserStoryFromBacklog(backlogId, userStoryId, userStory);

			return ResponseEntity.ok(Map.of("message", "User story updated successfully"));

		} catch (EntityNotFoundException e) {
			return ResponseEntity
					.status(HttpStatus.NOT_FOUND)
					.body(Map.of("error", e.getMessage()));
		} catch (ResponseStatusException e) {
			return ResponseEntity
					.status(HttpStatus.FORBIDDEN)
					.body(Map.of("error", e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("error", "Failed to update user story"));
		}
	}

	/**
	 * Delete user story from product backlog
	 */
	@DeleteMapping("/{backlogId}/user-stories/{userStoryId}")
	public ResponseEntity<?> deleteUserStory(
			@PathVariable Long backlogId,
			@PathVariable Long userStoryId,
			@RequestHeader("Authorization") String authHeader) {
		try {
			String token = authHeader.replace("Bearer ", "");
			String username = jwtUtil.extractUsername(token);
			Set<RoleType> roles = jwtUtil.extractRoles(token);

			Set<RoleType> authorizedRoles = Set.of(
					RoleType.PRODUCT_OWNER);
			projectService.assertAuthorized(roles, authorizedRoles);
			productBacklogService.isAuthorizedInBacklog(backlogId, username);

			productBacklogService.deleteUserStoryFromBacklog(backlogId, userStoryId);

			return ResponseEntity.ok(Map.of("message", "User story deleted successfully"));

		} catch (EntityNotFoundException e) {
			return ResponseEntity
					.status(HttpStatus.NOT_FOUND)
					.body(Map.of("error", e.getMessage()));
		} catch (ResponseStatusException e) {
			return ResponseEntity
					.status(HttpStatus.FORBIDDEN)
					.body(Map.of("error", e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("error", "Failed to delete user story"));
		}
	}

	/**
	 * Prioritize user story in product backlog
	 */
	@PatchMapping("/{backlogId}/user-stories/{userStoryId}/priority")
	public ResponseEntity<?> prioritizeUserStory(
			@PathVariable Long backlogId,
			@PathVariable Long userStoryId,
			@Valid @RequestBody PrioritizeUserStoryRequestDto request,
			@RequestHeader("Authorization") String authHeader) {
		try {
			String token = authHeader.replace("Bearer ", "");
			String username = jwtUtil.extractUsername(token);
			Set<RoleType> roles = jwtUtil.extractRoles(token);

			Set<RoleType> authorizedRoles = Set.of(
					RoleType.PRODUCT_OWNER);
			projectService.assertAuthorized(roles, authorizedRoles);
			productBacklogService.isAuthorizedInBacklog(backlogId, username);

			productBacklogService.prioritizeUserStoryFromBacklog(
					backlogId, userStoryId, request.getPriority());

			return ResponseEntity.ok(Map.of("message", "User story priority updated successfully"));

		} catch (EntityNotFoundException e) {
			return ResponseEntity
					.status(HttpStatus.NOT_FOUND)
					.body(Map.of("error", e.getMessage()));
		} catch (ResponseStatusException e) {
			return ResponseEntity
					.status(HttpStatus.FORBIDDEN)
					.body(Map.of("error", e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("error", "Failed to prioritize user story"));
		}
	}
}
