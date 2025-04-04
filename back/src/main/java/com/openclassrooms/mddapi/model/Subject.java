package com.openclassrooms.mddapi.model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "subjects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"posts", "abonnes"})
@EqualsAndHashCode(exclude = {"posts", "abonnes"})
public class Subject {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String nom;
    
    @OneToMany(mappedBy = "theme", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Post> posts = new HashSet<>();
    
    @ManyToMany(mappedBy = "abonnements")
    private Set<User> abonnes = new HashSet<>();
}
