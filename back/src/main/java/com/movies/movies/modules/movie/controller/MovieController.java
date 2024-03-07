package com.movies.movies.modules.movie.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movies.movies.config.ResponseApi;
import com.movies.movies.modules.movie.model.DtoDates;
import com.movies.movies.modules.movie.model.DtoMovie;
import com.movies.movies.modules.movie.model.Movie;
import com.movies.movies.security.service.CryptService;
import jakarta.validation.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/api/movie")
@CrossOrigin(origins = {"*"})
public class MovieController {

    private final CryptService cryptService;
    private final MovieService service;
    ObjectMapper objectMapper;

    @GetMapping("/page/")
    ResponseApi<?> getPage(Pageable pageable){
        try{
            Page<Movie> page = service.moviePage(pageable);
            String crypt = cryptService.encrypt(objectMapper.writeValueAsString(page));
            return new ResponseApi<>(crypt, HttpStatus.OK);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new ResponseApi<>(null, HttpStatus.INTERNAL_SERVER_ERROR, true, "internal server error");
        }

    }

    @GetMapping("/page/name/{name}")
    ResponseApi<?> getPageByName(@PathVariable(name = "name") String nameC, Pageable pageable){
        try{
            String name = cryptService.decrypt(nameC);
            Page<Movie> page = service.moviePageByName(name, pageable);
            String crypt = cryptService.encrypt(objectMapper.writeValueAsString(page));
            return new ResponseApi<>(crypt, HttpStatus.OK);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new ResponseApi<>(null, HttpStatus.INTERNAL_SERVER_ERROR, true, "internal server error");
        }
    }

    @GetMapping("/page/director/{name}")
    ResponseApi<?> getPageByDirector(@PathVariable(name = "name") String nameC, Pageable pageable){
        try{
            String name = cryptService.decrypt(nameC);
            Page<Movie> page = service.moviePageByDirector(name, pageable);
            String crypt = cryptService.encrypt(objectMapper.writeValueAsString(page));
            return new ResponseApi<>(crypt, HttpStatus.OK);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new ResponseApi<>(null, HttpStatus.INTERNAL_SERVER_ERROR, true, "internal server error");
        }
    }

    @GetMapping("/page/gender/{name}")
    ResponseApi<?> getPageByGender(@PathVariable(name = "name") String nameC, Pageable pageable){
        try{
            String name = cryptService.decrypt(nameC);
            Page<Movie> page = service.moviePageByGender(name, pageable);
            String crypt = cryptService.encrypt(objectMapper.writeValueAsString(page));
            return new ResponseApi<>(crypt, HttpStatus.OK);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new ResponseApi<>(null, HttpStatus.INTERNAL_SERVER_ERROR, true, "internal server error");
        }
    }

    @PostMapping("/page/betweenDates")
    ResponseApi<Page<Movie>> getPageBetweenTwoDates(@Valid @RequestBody DtoDates dates, Pageable pageable){
        return new ResponseApi<>( service.moviePageBetweenTwoDates(dates.getStart(), dates.getEnd(), pageable), HttpStatus.OK);
    }

    @PostMapping("/page/publication")
    ResponseApi<Page<Movie>> getPageByPublication(@Valid @RequestBody DtoDates dates, Pageable pageable){
        return new ResponseApi<>( service.moviePageByPublication(dates.getStart(), pageable), HttpStatus.OK);
    }

    @PostMapping("/")
    ResponseApi<?> insertMovie(@RequestBody String data) {
        try {
            String movieString = cryptService.decrypt(data);

            DtoMovie movie = objectMapper.readValue(movieString, DtoMovie.class);
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<DtoMovie>> violations = validator.validate(movie);

            if (!violations.isEmpty()) {
                Map<String, String> validationErrors = new HashMap<>();
                violations.forEach(violation -> validationErrors.put(violation.getPropertyPath() + "", violation.getMessage()));
                return new ResponseApi<>(null, HttpStatus.BAD_REQUEST, true, "bad request", validationErrors);
            }
            return new ResponseApi<>(service.insertMovie(movie), HttpStatus.OK);
        }catch (JsonProcessingException ex){
            return new ResponseApi<>(null, HttpStatus.BAD_REQUEST, true, "malformed request");
        } catch (Exception ex) {
            return new ResponseApi<>(null, HttpStatus.BAD_REQUEST, true, "failed crypt service");
        }
    }

    @GetMapping("/{id}")
    ResponseApi<?> getById(@PathVariable(name = "id") String idC){
        try{
            String id = cryptService.decrypt(idC);
            Optional<Movie> result = service.getById(Long.valueOf(id));
            return new ResponseApi<>(result, HttpStatus.OK);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new ResponseApi<>(null, HttpStatus.INTERNAL_SERVER_ERROR, true, "internal server error");
        }
    }

    @DeleteMapping("/{id}")
    ResponseApi<?> changeStatus(@PathVariable(name = "id") String idC){
        try{
            String id = cryptService.decrypt(idC);
            boolean result = service.changeStatus(Long.valueOf(id));
            Map<String, Boolean> data = new HashMap<>();
            data.put("result", result);
            return new ResponseApi<>(data, HttpStatus.OK);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new ResponseApi<>(null, HttpStatus.INTERNAL_SERVER_ERROR, true, "internal server error");
        }
    }

    @DeleteMapping("/delete/{id}")
    ResponseApi<?> deleteMovie(@PathVariable(name = "id") String idC){
        try{
            String id = cryptService.decrypt(idC);
            boolean result = service.deleteMovie(Long.valueOf(id));
            Map<String, Boolean> data = new HashMap<>();
            data.put("result", result);
            return new ResponseApi<>(data, HttpStatus.OK);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new ResponseApi<>(null, HttpStatus.INTERNAL_SERVER_ERROR, true, "internal server error");
        }
    }
}
