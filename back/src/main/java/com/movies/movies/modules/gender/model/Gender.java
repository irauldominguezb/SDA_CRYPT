package com.movies.movies.modules.gender.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.movies.movies.modules.movie.model.Movie;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Gender {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "gender")
    private List<Movie> movies;
}
