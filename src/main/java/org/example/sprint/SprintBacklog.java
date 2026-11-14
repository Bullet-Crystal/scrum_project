package org.example.sprint;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.example.backlog.UserStory;
import org.example.project.Project;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "sprint_backlogs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class SprintBacklog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Builder.Default
    @OneToMany(mappedBy = "sprintBacklog")
    private List<UserStory> userStories = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "sprintBacklog")
    private List<Task> tasks = new ArrayList<>();

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    public void addUserStory(UserStory userStory) {
        userStories.add(userStory);
        userStory.setSprintBacklog(this);
    }

    public void removeUserStory(UserStory userStory) {
        userStories.remove(userStory);
        userStory.setSprintBacklog(null);
    }

    public void addTask(Task task) {
        tasks.add(task);
        task.setSprintBacklog(this);
    }

    public void removeTask(Task task) {
        tasks.remove(task);
        task.setSprintBacklog(null);
    }
}
