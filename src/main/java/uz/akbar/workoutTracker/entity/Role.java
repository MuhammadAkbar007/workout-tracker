package uz.akbar.workoutTracker.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import uz.akbar.workoutTracker.enums.RoleType;

import java.util.Set;
import java.util.UUID;

/** Role */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "roles")
@Accessors(fluent = true)
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true)
    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    // @JsonIgnoreProperties("roles") // when serializing users, ignore their roles
    @ToString.Exclude // Prevents infinite recursion in toString()
    @EqualsAndHashCode.Exclude // Prevents infinite recursion in equals/hashCode
    @JsonIgnore
    @ManyToMany(mappedBy = "roles")
    private Set<User> users;
}
