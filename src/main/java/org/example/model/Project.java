package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.example.model.*;
import java.time.LocalDate;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "projects")
public class Project {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String title;
	private String description;
	@CreationTimestamp
	private LocalDate creationDate;
	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
	private List<SprintBacklog> sprintBacklogs;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "product_backlog_id", referencedColumnName = "id")
	private ProductBacklog productBacklog;
	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
	private List<User> users;
}
