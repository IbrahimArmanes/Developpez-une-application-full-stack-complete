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
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"abonnements", "posts", "comments"})
@EqualsAndHashCode(exclude = {"abonnements", "posts", "comments"})
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @Column(unique = true, nullable = false)
    private String username;
    
    @Column(nullable = false)
    private String password;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "user_subjects",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    private Set<Subject> abonnements = new HashSet<>();
    
    @OneToMany(mappedBy = "auteur", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Post> posts = new HashSet<>();
    
    @OneToMany(mappedBy = "auteur", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments = new HashSet<>();
}
