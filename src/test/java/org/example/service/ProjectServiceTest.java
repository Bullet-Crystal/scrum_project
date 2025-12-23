package org.example.service;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.example.model.Project;
import org.example.repository.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class ProjectServiceImpTest {

	@Mock
	private ProjectRepository projectRepo;

	@InjectMocks
	private ProjectServiceImp projectService;

	// ----------------------------
	// createProject
	// ----------------------------

	@Test
	void createProject_shouldCreateAndLinkProductBacklog_andSaveProject() {
		Project project = new Project();

		when(projectRepo.save(project)).thenReturn(project);

		Project savedProject = projectService.createProject(project);

		assertThat(savedProject).isEqualTo(project);
		assertThat(project.getProductBacklog()).isNotNull();
		assertThat(project.getProductBacklog().getProject()).isEqualTo(project);

		verify(projectRepo).save(project);
	}

	// ----------------------------
	// updateProject
	// ----------------------------

	@Test
	void updateProject_shouldUpdateFields_andSave() {
		Project existingProject = new Project();
		existingProject.setTitle("Old title");

		Project newProject = new Project();
		newProject.setTitle("New title");

		when(projectRepo.findById(1L))
				.thenReturn(Optional.of(existingProject));
		when(projectRepo.save(existingProject))
				.thenReturn(existingProject);

		Project updatedProject = projectService.updateProject(1L, newProject);

		assertThat(updatedProject.getTitle()).isEqualTo("New title");
		verify(projectRepo).save(existingProject);
	}

	// ----------------------------
	// deleteProjectById
	// ----------------------------

	@Test
	void deleteProjectById_shouldDeleteProject() {
		Project project = new Project();

		when(projectRepo.findById(1L))
				.thenReturn(Optional.of(project));

		projectService.deleteProjectById(1L);

		verify(projectRepo).delete(project);
	}

	// ----------------------------
	// getProjectById
	// ----------------------------

	@Test
	void getProjectById_shouldReturnProject_whenExists() {
		Project project = new Project();

		when(projectRepo.findById(1L))
				.thenReturn(Optional.of(project));

		Project result = projectService.getProjectById(1L);

		assertThat(result).isEqualTo(project);
	}

	@Test
	void getProjectById_shouldThrow_whenNotFound() {
		when(projectRepo.findById(1L))
				.thenReturn(Optional.empty());

		assertThatThrownBy(() -> projectService.getProjectById(1L))
				.isInstanceOf(EntityNotFoundException.class)
				.hasMessageContaining("Project not found");
	}

	// ----------------------------
	// getAllProjects
	// ----------------------------

	@Test
	void getAllProjects_shouldReturnAllProjects() {
		List<Project> projects = List.of(
				new Project(),
				new Project());

		when(projectRepo.findAll()).thenReturn(projects);

		List<Project> result = projectService.getAllProjects();

		assertThat(result).hasSize(2);
		assertThat(result).isEqualTo(projects);
	}
}
