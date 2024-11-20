package uz.akbar.workoutTracker.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/** Workout */
@Entity
@Data
public class Workout {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private LocalDateTime scheduledDateTime;

    @Column(columnDefinition = "text")
    private String comment;

    @ManyToOne private User user;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Exercise> exercises = new ArrayList<>();
}
