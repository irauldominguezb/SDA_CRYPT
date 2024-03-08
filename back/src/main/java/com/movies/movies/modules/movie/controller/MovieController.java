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
import org.springframework.http.ResponseEntity;
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
    ResponseEntity<ResponseApi<?>> getPage(Pageable pageable){
        try{
            Page<Movie> page = service.moviePage(pageable);
            // Convertir la página de películas a formato JSON y cifrar la cadena resultante
            String crypt = cryptService.encrypt(objectMapper.writeValueAsString(page));
            // Devolver una respuesta exitosa con la cadena cifrada
            return new ResponseEntity<>(new ResponseApi<>(crypt, HttpStatus.OK), HttpStatus.OK);
        }catch (Exception e){
            // En caso de error, imprimir el mensaje de la excepción en la consola
            System.out.println(e.getMessage());
            return new ResponseEntity<>(new ResponseApi<>(null, HttpStatus.INTERNAL_SERVER_ERROR, true, "internal server error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/page/name/{name}")
    ResponseEntity <ResponseApi<?>> getPageByName(@PathVariable(name = "name") String nameC, Pageable pageable){
        try{
            String name = cryptService.decrypt(nameC);
            Page<Movie> page = service.moviePageByName(name, pageable);
            String crypt = cryptService.encrypt(objectMapper.writeValueAsString(page));
            return new ResponseEntity<>( new ResponseApi<>(crypt, HttpStatus.OK), HttpStatus.OK);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>( new ResponseApi<>(null, HttpStatus.INTERNAL_SERVER_ERROR, true, "internal server error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/page/director/{name}")
    ResponseEntity <ResponseApi<?>> getPageByDirector(@PathVariable(name = "name") String nameC, Pageable pageable){
        try{
            String name = cryptService.decrypt(nameC);
            Page<Movie> page = service.moviePageByDirector(name, pageable);
            String crypt = cryptService.encrypt(objectMapper.writeValueAsString(page));
            return new ResponseEntity<>( new ResponseApi<>(crypt, HttpStatus.OK), HttpStatus.OK);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>( new ResponseApi<>(null, HttpStatus.INTERNAL_SERVER_ERROR, true, "internal server error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/page/gender/{name}")
    ResponseEntity <ResponseApi<?>> getPageByGender(@PathVariable(name = "name") String nameC, Pageable pageable){
        try{
            String name = cryptService.decrypt(nameC);
            Page<Movie> page = service.moviePageByGender(name, pageable);
            String crypt = cryptService.encrypt(objectMapper.writeValueAsString(page));
            return new ResponseEntity<>( new ResponseApi<>(crypt, HttpStatus.OK), HttpStatus.OK);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>( new ResponseApi<>(null, HttpStatus.INTERNAL_SERVER_ERROR, true, "internal server error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/page/betweenDates")
    ResponseEntity <ResponseApi<Page<Movie>>> getPageBetweenTwoDates(@Valid @RequestBody DtoDates dates, Pageable pageable){
        return new ResponseEntity<>( new ResponseApi<>( service.moviePageBetweenTwoDates(dates.getStart(), dates.getEnd(), pageable), HttpStatus.OK), HttpStatus.OK);
    }

    @PostMapping("/page/publication")
    ResponseEntity <ResponseApi<Page<Movie>>> getPageByPublication(@Valid @RequestBody DtoDates dates, Pageable pageable){
        return new ResponseEntity<>( new ResponseApi<>( service.moviePageByPublication(dates.getStart(), pageable), HttpStatus.OK), HttpStatus.OK);
    }

    @PostMapping("/")
    ResponseEntity <ResponseApi<?>> insertMovie(@RequestBody String data) {
        try {
            // Desencripta la cadena de datos recibida
            String movieString = cryptService.decrypt(data);

            // Convierte la cadena desencriptada a un objeto DtoMovie utilizando Json ObjectMapper
            DtoMovie movie = objectMapper.readValue(movieString, DtoMovie.class);
            // Valida el objeto DtoMovie utilizando el sistema de validación de Bean Validation
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<DtoMovie>> violations = validator.validate(movie);

            // Si hay violaciones de las restricciones de validación, devulve una respuesta con errores de validación
            if (!violations.isEmpty()) {
                Map<String, String> validationErrors = new HashMap<>();
                violations.forEach(violation -> validationErrors.put(violation.getPropertyPath() + "", violation.getMessage()));
                return new ResponseEntity<>( new ResponseApi<>(null, HttpStatus.BAD_REQUEST, true, "bad request", validationErrors), HttpStatus.BAD_REQUEST);
            }
            // Si la validación es exitosa, inserta la película utilizando el servicio y devuelve una respuesta exitosa
            return new ResponseEntity<>( new ResponseApi<>(service.insertMovie(movie), HttpStatus.OK), HttpStatus.OK);
        }catch (JsonProcessingException ex){
            // Maneja la excepción en caso de que haya un problema al procesar el JSON recibido
            return new ResponseEntity<>( new ResponseApi<>(null, HttpStatus.BAD_REQUEST, true, "malformed request"), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            // Maneja la excepción en caso de un error general, por ejemplo, fallo en el servicio de cifrado
            return new ResponseEntity<>( new ResponseApi<>(null, HttpStatus.INTERNAL_SERVER_ERROR, true, "failed crypt service"), HttpStatus.INTERNAL_SERVER_ERROR);
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
