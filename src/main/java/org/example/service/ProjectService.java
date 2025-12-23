package org.example.service;

import org.example.model.Project;

import java.util.List;

public interface ProjectService {
	Project getProjectById(Long id);

	Project createProject(Project project);

	Project updateProject(Long id, Project newProject);

	void deleteProjectById(Long id);

	List<Project> getAllProjects();
}
