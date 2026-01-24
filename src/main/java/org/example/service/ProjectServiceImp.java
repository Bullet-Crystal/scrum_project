package org.example.service;

import java.util.List;
import java.util.Set;

import org.example.model.ProductBacklog;
import org.example.model.Project;
import org.example.model.RoleType;
import org.example.repository.ProjectRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectServiceImp implements ProjectService {

	private final ProjectRepository projectRepo;

	@Transactional
	public Project createProject(Project project) {
		ProductBacklog productBacklog = ProductBacklog.builder()
				.title("Backlog for " + project.getTitle())
				.build();

		project.setProductBacklog(productBacklog);
		productBacklog.setProject(project);

		return projectRepo.save(project);
	}

	@Transactional
	public Project updateProject(Long id, Project newProject) {
		Project project = getProjectById(id);
		project.setTitle(newProject.getTitle());
		project.setProductBacklog(newProject.getProductBacklog());
		project.setSprintBacklogs(newProject.getSprintBacklogs());
		return projectRepo.save(project);
	}

	@Transactional
	public void deleteProjectById(Long id) {
		Project project = getProjectById(id);
		projectRepo.delete(project);
	}

	public void assertAuthorized(Set<RoleType> roles, Set<RoleType> authorizedRoles) {

		boolean authorized = roles.stream()
				.anyMatch(authorizedRoles::contains);
		System.out.println(authorizedRoles);
		System.out.println(roles);

		if (!authorized) {
			throw new ResponseStatusException(
					HttpStatus.UNAUTHORIZED,
					"Access denied. Required role: " + authorizedRoles);
		}
	}

	public void isUserAuthorized(Long projectId, String username) {
		boolean isAuthorized = getProjectById(projectId).getUsers().stream()
				.anyMatch(user -> user.getUsername().equals(username));

		if (!isAuthorized) {
			throw new ResponseStatusException(
					HttpStatus.FORBIDDEN,
					"Access denied. You are not authorized to access this project.");
		}
	}

	public Project getProjectById(Long id) {
		return projectRepo.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Project not found"));
	}

	public List<Project> getAllProjects() {
		return projectRepo.findAll();

	}
}
