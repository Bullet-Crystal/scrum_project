package org.example.backlog;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.example.sprint.SprintBacklog;
import org.example.sprint.Status;
import org.example.sprint.Task;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_stories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserStory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "role", length = 100)
    private String role;

    @Column(name = "action", length = 500)
    private String action;

    @Column(name = "goal", length = 500)
    private String goal;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false, length = 30)
    private Priority priority;

    @CreationTimestamp
    @Column(name = "creation_date", nullable = false, updatable = false)
    private LocalDate creationDate;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private Status status = Status.TO_DO;

    @Column(name = "critere_acceptation", length = 500)
    private String critereAcceptation;

    @Builder.Default
    @OneToMany(mappedBy = "userStory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "epic_id")
    private Epic epic;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_backlog_id", nullable = false)
    private ProductBacklog productBacklog;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sprint_backlog_id")
    private SprintBacklog sprintBacklog;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    public String getDescription() {
        return "En tant que " + role + ", je veux " + action + " afin de " + goal;
    }

    public void setDescription(String role, String action, String goal) {
        this.role = role;
        this.action = action;
        this.goal = goal;
    }

    public void addTask(Task task) {
        tasks.add(task);
        task.setUserStory(this);
    }

    public void removeTask(Task task) {
        tasks.remove(task);
        task.setUserStory(null);
    }
}
