package com.movies.movies.modules.movie.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface MovieRepository extends JpaRepository<Movie,Long> {

    Page<Movie> findMoviesByNameContaining(String name, Pageable pageable);

    Page<Movie> findMoviesByDirectorContaining(String name, Pageable pageable);

    Page<Movie> findMoviesByGender_Name(String name, Pageable pageable);

    Page<Movie> findMoviesByPublicationBetween(Date start, Date end, Pageable pageable);

    Page<Movie> findMoviesByPublicationOrderByPublicationDesc(Date publication, Pageable pageable);
    @Modifying
    @Query("update Movie SET available = :available where id = :id")
    void updateStatus (@Param("available") boolean available, @Param("id") Long id);
}
