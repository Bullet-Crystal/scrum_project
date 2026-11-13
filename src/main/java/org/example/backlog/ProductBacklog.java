package org.example.backlog;

import java.util.ArrayList;
import java.util.List;

import org.example.project.Project;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "product_backlogs", uniqueConstraints = @UniqueConstraint(
        name = "uk_product_backlogs_project", columnNames = "project_id"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ProductBacklog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id", nullable = false, unique = true)
    private Project project;

    @Builder.Default
    @OneToMany(mappedBy = "productBacklog", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Epic> epics = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "productBacklog", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserStory> userStories = new ArrayList<>();

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    public void addEpic(Epic epic) {
        epics.add(epic);
        epic.setProductBacklog(this);
    }

    public void removeEpic(Epic epic) {
        epics.remove(epic);
        epic.setProductBacklog(null);
    }

    public void addUserStory(UserStory userStory) {
        userStories.add(userStory);
        userStory.setProductBacklog(this);
    }

    public void removeUserStory(UserStory userStory) {
        userStories.remove(userStory);
        userStory.setProductBacklog(null);
    }
}
