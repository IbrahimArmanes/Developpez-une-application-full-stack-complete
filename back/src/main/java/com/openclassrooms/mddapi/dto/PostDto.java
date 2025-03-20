package com.openclassrooms.mddapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
    private Long id;
    private String titre;
    private String contenu;
    private LocalDateTime date;
    private UserSimpleDto auteur;
    private SubjectSimpleDto theme;
    private Set<CommentDto> commentaires = new HashSet<>();
}
