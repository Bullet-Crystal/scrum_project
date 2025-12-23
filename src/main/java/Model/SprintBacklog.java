package Model;

import lombok.*;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class SprintBacklog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;
    private String nom;
    private Date dateDebut;
    private Date dateFin;
    @OneToMany(mappedBy = "sprintBacklog", cascade = CascadeType.ALL)
    List<UserStory> userStories;
    @OneToMany(mappedBy = "sprintBacklog", cascade = CascadeType.ALL)
    List<Task> tasks;

    public void addUserStory(UserStory userStory) {
        this.userStories.add(userStory);
    }
    public void addTask(Task task) {
        this.tasks.add(task);
    }
    public void removeUserStory(UserStory userStory) {
        this.userStories.remove(userStory);
    }
    public void removeTask(Task task) {
        this.tasks.remove(task);
    }
}
