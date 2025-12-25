package org.example.model;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titre;
    private String description;
    @ManyToOne
    @JoinColumn(name = "user_story_id")
    UserStory userStory;
    Statut taskStatut = Statut.TO_DO;
}
