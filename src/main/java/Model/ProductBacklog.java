package Model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ProductBacklog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nom;
    @OneToMany(mappedBy = "productBacklog", cascade = CascadeType.ALL)
    List<Epic> epics;
    @OneToMany(mappedBy = "productBacklog", cascade = CascadeType.ALL)
    List<UserStory> userStories;
}
