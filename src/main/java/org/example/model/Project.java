package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Project {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Id;
	private String title;
	@CreationTimestamp
	private Date dateDeCreation;
	@OneToMany(mappedBy = "sprintBacklog", cascade = CascadeType.ALL)
	private List<SprintBacklog> sprintBacklogs;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "product_backlog_id", referencedColumnName = "id")
	private ProductBacklog productBacklog;
}
