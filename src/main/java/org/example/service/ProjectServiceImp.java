package org.example.service;

import java.util.List;

import org.example.model.ProductBacklog;
import org.example.model.Project;
import org.example.repository.ProductBacklogRepository;
import org.example.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectServiceImp implements ProjectService {

	private final ProjectRepository projectRepo;

	@Override
	@Transactional
	public Project createProject(Project project) {
		ProductBacklog productBacklog = new ProductBacklog();
		project.setProductBacklog(productBacklog);
		productBacklog.setProject(project);
		return projectRepo.save(project);
	}

	@Override
	@Transactional
	public Project updateProject(Long id, Project newProject) {
		Project project = getProjectById(id);
		project.setTitle(newProject.getTitle());
		project.setProductBacklog(newProject.getProductBacklog());
		project.setSprintBacklogs(newProject.getSprintBacklogs());
		return projectRepo.save(project);
	}

	@Override
	@Transactional
	public void deleteProjectById(Long id) {
		Project project = getProjectById(id);
		projectRepo.delete(project);
	}

	@Override
	public Project getProjectById(Long id) {
		return projectRepo.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Project not found"));
	}

	@Override
	public List<Project> getAllProjects() {
		return projectRepo.findAll();

	}
}
