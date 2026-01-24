package org.example.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.example.dto.*;
import org.example.mapper.ProjectMapper;
import org.example.model.Project;
import org.example.model.RoleType;
import org.example.model.User;
import org.example.security.JwtUtil;
import org.example.service.ProjectService;
import org.example.service.UserService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {
	private final ProjectService projectService;
	private final UserService userService;
	private final JwtUtil jwtUtil;
	private final ProjectMapper projectMapper;

	@PostMapping
	public ResponseEntity<?> createProject(
			@Valid @RequestBody CreateProjectRequestDto request,
			@RequestHeader("Authorization") String authHeader) {
		try {
			// Extract roles from JWT token
			String token = authHeader.replace("Bearer ", "");
			Set<RoleType> roles = jwtUtil.extractRoles(token);
			String username = jwtUtil.extractUsername(token);

			Set<RoleType> authorizedRoles = Set.of(
					RoleType.PRODUCT_OWNER);
			projectService.assertAuthorized(roles, authorizedRoles);

			// Create project
			String title = request.getTitle();
			String description = request.getDescription();
			LocalDate creationDate = LocalDate.now();

			User user = userService.getUserByUsername(username);

			Project project = Project.builder()
					.title(title)
					.users(new ArrayList<>(List
							.of(user)))
					.description(description)
					.creationDate(creationDate)
					.build();

			user.setProject(project);
			projectService.createProject(project);

			// Build response
			ProjectResponseDto response = projectMapper.toProjectResponse(project);

			return ResponseEntity
					.status(HttpStatus.CREATED)
					.body(response);

		} catch (ResponseStatusException e) {
			return ResponseEntity
					.status(e.getStatusCode())
					.body(Map.of("error", e.getReason()));
		} catch (DuplicateKeyException e) {
			return ResponseEntity
					.status(HttpStatus.CONFLICT)
					.body(Map.of("error", e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("error", "Failed to create project"));
			// .body(Map.of("error", e.getMessage()));
		}
	}

	@GetMapping
	public ResponseEntity<?> getMyProjects(
			@RequestHeader("Authorization") String authHeader) {
		try {
			List<Project> projects = projectService.getAllProjects();

			List<ProjectResponseDto> response = projectMapper.toProjectResponseList(projects);

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("error", "Failed to retrieve projects"));
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getProject(
			@PathVariable Long id,
			@RequestHeader("Authorization") String authHeader) {
		try {
			Project project = projectService.getProjectById(id);

			ProjectResponseDto response = projectMapper.toProjectResponse(project);

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("error", "Failed to retrieve project"));
		}
	}
}
