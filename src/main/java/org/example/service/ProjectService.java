package org.example.service;

import org.example.model.Project;
import org.example.model.RoleType;

import java.util.List;
import java.util.Set;

public interface ProjectService {
	Project getProjectById(Long id);

	Project createProject(Project project);

	Project updateProject(Long id, Project newProject);

	void assertAuthorized(Set<RoleType> roles, Set<RoleType> authorizedRoles);

	void isUserAuthorized(Long projectId, String username);

	void deleteProjectById(Long id);

	List<Project> getAllProjects();
}
