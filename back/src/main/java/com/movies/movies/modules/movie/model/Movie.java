package com.movies.movies.modules.movie.model;

import com.movies.movies.modules.gender.model.Gender;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String duration;

    @Column(nullable = false)
    private String director;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date publication;

    @Column
    private boolean available = true;

    @ManyToOne
    @JoinColumn(name = "gender_id", nullable = false)
    private Gender gender;

    public void setDataMovie(DtoMovie movie){
        setId(movie.getId());
        setName(movie.getName());
        setAvailable(movie.isAvailable());
        setDuration(movie.getDuration());
        setDirector(movie.getDirector());
        setPublication(movie.getPublication());
        Gender gen = new Gender();
        gen.setId(movie.getGender());
        setGender(gen);
    }
}
