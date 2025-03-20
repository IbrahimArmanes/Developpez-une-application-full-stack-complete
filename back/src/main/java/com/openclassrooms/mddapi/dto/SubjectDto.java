package com.openclassrooms.mddapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubjectDto {
    private Long id;
    private String nom;
    private Set<PostSimpleDto> posts = new HashSet<>();
    private Set<UserSimpleDto> abonnes = new HashSet<>();
}
