package org.example.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.example.dto.CreateEpicRequestDto;
import org.example.dto.EpicResponseDto;
import org.example.dto.UpdateEpicRequestDto;
import org.example.dto.UserStoryResponseDto;
import org.example.mapper.EpicMapper;
import org.example.model.Epic;
import org.example.model.RoleType;
import org.example.model.UserStory;
import org.example.security.JwtUtil;
import org.example.service.EpicService;
import org.example.service.ProductBacklogService;
import org.example.service.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException.Unauthorized;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/product-backlogs/{backlogId}/epics")
@RequiredArgsConstructor
public class EpicController {
	private final EpicService epicService;
	private final EpicMapper epicMapper;
	private final ProjectService projectService;
	private final ProductBacklogService productBacklogService;
	private final JwtUtil jwtUtil;

	/**
	 * Create a new epic
	 */
	@PostMapping
	public ResponseEntity<?> createEpic(
			@PathVariable Long backlogId,
			@Valid @RequestBody CreateEpicRequestDto request,
			@RequestHeader("Authorization") String authHeader) {
		try {
			String token = authHeader.replace("Bearer ", "");
			String username = jwtUtil.extractUsername(token);
			Set<RoleType> roles = jwtUtil.extractRoles(token);

			// Only PRODUCT_OWNER can create epics
			Set<RoleType> authorizedRoles = Set.of(
					RoleType.PRODUCT_OWNER);
			projectService.assertAuthorized(roles, authorizedRoles);
			productBacklogService.isAuthorizedInBacklog(backlogId, username);

			Epic epic = epicMapper.toEpic(request);
			Epic createdEpic = epicService.createEpic(backlogId, epic);
			EpicResponseDto response = epicMapper.toEpicResponse(createdEpic);

			return ResponseEntity
					.status(HttpStatus.CREATED)
					.body(response);

		} catch (Unauthorized e) {
			return ResponseEntity
					.status(HttpStatus.FORBIDDEN)
					.body(Map.of("error", e.getMessage()));
		} catch (EntityNotFoundException e) {
			return ResponseEntity
					.status(HttpStatus.NOT_FOUND)
					.body(Map.of("error", e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("error", "Failed to create epic"));
		}
	}

	/**
	 * Get epic by ID
	 */
	@GetMapping("/{epicId}")
	public ResponseEntity<?> getEpic(
			@PathVariable Long backlogId,
			@PathVariable Long epicId,
			@RequestHeader("Authorization") String authHeader) {
		try {
			String token = authHeader.replace("Bearer ", "");
			String username = jwtUtil.extractUsername(token);
			Set<RoleType> roles = jwtUtil.extractRoles(token);

			// Any authenticated user with proper role can view epics
			Set<RoleType> authorizedRoles = Set.of(
					RoleType.DEVELOPER,
					RoleType.SCRUM_MASTER,
					RoleType.PRODUCT_OWNER);
			projectService.assertAuthorized(roles, authorizedRoles);
			productBacklogService.isAuthorizedInBacklog(backlogId, username);

			Epic epic = epicService.getEpicById(epicId);
			EpicResponseDto response = epicMapper.toEpicResponse(epic);

			return ResponseEntity.ok(response);

		} catch (Unauthorized e) {
			return ResponseEntity
					.status(HttpStatus.FORBIDDEN)
					.body(Map.of("error", e.getMessage()));
		} catch (EntityNotFoundException e) {
			return ResponseEntity
					.status(HttpStatus.NOT_FOUND)
					.body(Map.of("error", e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("error", "Failed to retrieve epic"));
		}
	}

	/**
	 * Update epic
	 */
	@PutMapping("/{epicId}")
	public ResponseEntity<?> updateEpic(
			@PathVariable Long backlogId,
			@PathVariable Long epicId,
			@Valid @RequestBody UpdateEpicRequestDto request,
			@RequestHeader("Authorization") String authHeader) {
		try {
			String token = authHeader.replace("Bearer ", "");
			String username = jwtUtil.extractUsername(token);
			Set<RoleType> roles = jwtUtil.extractRoles(token);

			// Only PRODUCT_OWNER can update epics
			Set<RoleType> authorizedRoles = Set.of(
					RoleType.PRODUCT_OWNER);
			projectService.assertAuthorized(roles, authorizedRoles);
			productBacklogService.isAuthorizedInBacklog(backlogId, username);

			Epic existingEpic = epicService.getEpicById(epicId);
			epicMapper.updateEpicFromRequest(existingEpic, request);
			epicService.updateEpic(epicId, existingEpic);

			return ResponseEntity.ok(Map.of("message", "Epic updated successfully"));

		} catch (Unauthorized e) {
			return ResponseEntity
					.status(HttpStatus.FORBIDDEN)
					.body(Map.of("error", e.getMessage()));
		} catch (EntityNotFoundException e) {
			return ResponseEntity
					.status(HttpStatus.NOT_FOUND)
					.body(Map.of("error", e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("error", "Failed to update epic"));
		}
	}

	/**
	 * Delete epic
	 */
	@DeleteMapping("/{epicId}")
	public ResponseEntity<?> deleteEpic(
			@PathVariable Long backlogId,
			@PathVariable Long epicId,
			@RequestHeader("Authorization") String authHeader) {
		try {
			String token = authHeader.replace("Bearer ", "");
			String username = jwtUtil.extractUsername(token);
			Set<RoleType> roles = jwtUtil.extractRoles(token);

			// Only PRODUCT_OWNER can delete epics
			Set<RoleType> authorizedRoles = Set.of(
					RoleType.PRODUCT_OWNER);
			projectService.assertAuthorized(roles, authorizedRoles);
			productBacklogService.isAuthorizedInBacklog(backlogId, username);

			epicService.deleteEpic(epicId);

			return ResponseEntity.ok(Map.of("message", "Epic deleted successfully"));

		} catch (Unauthorized e) {
			return ResponseEntity
					.status(HttpStatus.FORBIDDEN)
					.body(Map.of("error", e.getMessage()));
		} catch (EntityNotFoundException e) {
			return ResponseEntity
					.status(HttpStatus.NOT_FOUND)
					.body(Map.of("error", e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("error", "Failed to delete epic"));
		}
	}

	/**
	 * Add user story to epic
	 */
	@PostMapping("/{epicId}/user-stories/{userStoryId}")
	public ResponseEntity<?> addUserStoryToEpic(
			@PathVariable Long backlogId,
			@PathVariable Long epicId,
			@PathVariable Long userStoryId,
			@RequestHeader("Authorization") String authHeader) {
		try {
			String token = authHeader.replace("Bearer ", "");
			String username = jwtUtil.extractUsername(token);
			Set<RoleType> roles = jwtUtil.extractRoles(token);

			// Only PRODUCT_OWNER can add user stories to epics
			Set<RoleType> authorizedRoles = Set.of(
					RoleType.PRODUCT_OWNER);
			projectService.assertAuthorized(roles, authorizedRoles);
			productBacklogService.isAuthorizedInBacklog(backlogId, username);

			epicService.addUserStoryToEpic(epicId, userStoryId);

			return ResponseEntity.ok(Map.of("message", "User story added to epic successfully"));

		} catch (Unauthorized e) {
			return ResponseEntity
					.status(HttpStatus.FORBIDDEN)
					.body(Map.of("error", e.getMessage()));
		} catch (EntityNotFoundException e) {
			return ResponseEntity
					.status(HttpStatus.NOT_FOUND)
					.body(Map.of("error", e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("error", "Failed to add user story to epic"));
		}
	}

	/**
	 * Remove user story from epic
	 */
	@DeleteMapping("/{epicId}/user-stories/{userStoryId}")
	public ResponseEntity<?> removeUserStoryFromEpic(
			@PathVariable Long backlogId,
			@PathVariable Long epicId,
			@PathVariable Long userStoryId,
			@RequestHeader("Authorization") String authHeader) {
		try {
			String token = authHeader.replace("Bearer ", "");
			String username = jwtUtil.extractUsername(token);
			Set<RoleType> roles = jwtUtil.extractRoles(token);

			// Only PRODUCT_OWNER can remove user stories from epics
			Set<RoleType> authorizedRoles = Set.of(
					RoleType.PRODUCT_OWNER);
			projectService.assertAuthorized(roles, authorizedRoles);
			productBacklogService.isAuthorizedInBacklog(backlogId, username);

			epicService.removeUserStoryFromEpic(epicId, userStoryId);

			return ResponseEntity.ok(Map.of("message", "User story removed from epic successfully"));

		} catch (Unauthorized e) {
			return ResponseEntity
					.status(HttpStatus.FORBIDDEN)
					.body(Map.of("error", e.getMessage()));
		} catch (EntityNotFoundException e) {
			return ResponseEntity
					.status(HttpStatus.NOT_FOUND)
					.body(Map.of("error", e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("error", "Failed to remove user story from epic"));
		}
	}

	/**
	 * Get all user stories from epic
	 */
	@GetMapping("/{epicId}/user-stories")
	public ResponseEntity<?> getUserStoriesFromEpic(
			@PathVariable Long backlogId,
			@PathVariable Long epicId,
			@RequestHeader("Authorization") String authHeader) {
		try {
			String token = authHeader.replace("Bearer ", "");
			String username = jwtUtil.extractUsername(token);
			Set<RoleType> roles = jwtUtil.extractRoles(token);

			// Any authenticated user with proper role can view user stories
			Set<RoleType> authorizedRoles = Set.of(
					RoleType.DEVELOPER,
					RoleType.SCRUM_MASTER,
					RoleType.PRODUCT_OWNER);
			projectService.assertAuthorized(roles, authorizedRoles);
			productBacklogService.isAuthorizedInBacklog(backlogId, username);

			List<UserStory> userStories = epicService.getUserStoriesFromEpic(epicId);
			List<UserStoryResponseDto> response = userStories.stream()
					.map(epicMapper::toUserStoryResponse)
					.collect(Collectors.toList());

			return ResponseEntity.ok(response);

		} catch (Unauthorized e) {
			return ResponseEntity
					.status(HttpStatus.FORBIDDEN)
					.body(Map.of("error", e.getMessage()));
		} catch (EntityNotFoundException e) {
			return ResponseEntity
					.status(HttpStatus.NOT_FOUND)
					.body(Map.of("error", e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("error", "Failed to retrieve user stories"));
		}
	}

	/**
	 * Visualize user stories from epic
	 */
	@GetMapping("/{epicId}/user-stories/visualize")
	public ResponseEntity<?> visualizeUserStoriesFromEpic(
			@PathVariable Long backlogId,
			@PathVariable Long epicId,
			@RequestHeader("Authorization") String authHeader) {
		try {
			String token = authHeader.replace("Bearer ", "");
			String username = jwtUtil.extractUsername(token);
			Set<RoleType> roles = jwtUtil.extractRoles(token);

			// Any authenticated user with proper role can visualize
			Set<RoleType> authorizedRoles = Set.of(
					RoleType.DEVELOPER,
					RoleType.SCRUM_MASTER,
					RoleType.PRODUCT_OWNER);
			projectService.assertAuthorized(roles, authorizedRoles);
			productBacklogService.isAuthorizedInBacklog(backlogId, username);

			String visualization = epicService.vizualizeUserStoriesFromEpic(epicId);

			return ResponseEntity.ok(Map.of("visualization", visualization));

		} catch (Unauthorized e) {
			return ResponseEntity
					.status(HttpStatus.FORBIDDEN)
					.body(Map.of("error", e.getMessage()));
		} catch (EntityNotFoundException e) {
			return ResponseEntity
					.status(HttpStatus.NOT_FOUND)
					.body(Map.of("error", e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("error", "Failed to visualize user stories"));
		}
	}
}
