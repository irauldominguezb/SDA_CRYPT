package com.movies.movies.modules.movie.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class DtoMovie {

    // Se aplican validaciones en la mayoría de los campos para asegurar
    // tamaños adecuados, formato de fecha correcto y evitar valores nulos.

    private Long id;

    @NotNull(message = "missing field")
    @NotBlank(message = "empty field")
    @Length(min = 3, max = 100)
    private String name;

    @NotNull(message = "missing field")
    @NotBlank(message = "empty field")
    @Length(min = 5, max = 10)
    private String duration;

    @NotNull(message = "missing field")
    @NotBlank(message = "empty field")
    @Length(min = 3, max = 100)
    private String director;

    @NotNull(message = "missing field")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date publication;

    private boolean available = true;

    @NotNull(message = "missing field")
    @Min(value = 1, message = "invalid field")
    private Long gender;
}
