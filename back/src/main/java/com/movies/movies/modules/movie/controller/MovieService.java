package com.movies.movies.modules.movie.controller;

import com.movies.movies.modules.movie.model.DtoMovie;
import com.movies.movies.modules.movie.model.Movie;
import com.movies.movies.modules.movie.model.MovieRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class MovieService {

    @Autowired
    private MovieRepository repository;

    public Page<Movie> moviePage (Pageable pageable){ return repository.findAll(pageable);}

    public Page<Movie> moviePageByName (String name, Pageable pageable){
        return repository.findMoviesByNameContaining(name, pageable);
    }

    public Page<Movie> moviePageByDirector (String name, Pageable pageable){
        return repository.findMoviesByDirectorContaining(name, pageable);
    }

    public Page<Movie> moviePageByGender (String name, Pageable pageable){
        return repository.findMoviesByGender_Name(name, pageable);
    }

    public Page<Movie> moviePageBetweenTwoDates (Date start, Date end, Pageable pageable){
        return repository.findMoviesByPublicationBetween(start, end, pageable);
    }

    public Page<Movie> moviePageByPublication (Date publication, Pageable pageable){
        return repository.findMoviesByPublicationOrderByPublicationDesc(publication, pageable);
    }

    public Optional<Movie> getById (Long id){
        return repository.findById(id);
    }

    @Transactional
    public Movie insertMovie (DtoMovie movie){
        Movie mov = new Movie();
        mov.setDataMovie(movie);
        return repository.save(mov);
    }

    @Transactional
    public boolean changeStatus(Long id){
        try{
            Optional<Movie> movie = repository.findById(id);
            repository.updateStatus(!movie.get().isAvailable(), id);
        }catch (Exception ex){
            return false;
        }
        return true;
    }

    @Transactional
    public boolean deleteMovie(Long id){
        try{
            repository.deleteById(id);
        }catch (Exception ex){
            return false;
        }
        return true;
    }
}
