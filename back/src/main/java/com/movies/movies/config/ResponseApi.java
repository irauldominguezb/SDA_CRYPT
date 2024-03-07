package com.movies.movies.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResponseApi<T> {
    //Respuesta personalizada
    private T data;
    private HttpStatus status;
    private boolean error;
    private String msg;
    private Map<String, String> errors;

    //Respuesta basica
    public ResponseApi(T data, HttpStatus status) {
        this.data = data;
        this.status = status;
    }

    //Respuesta completa
    public ResponseApi(T data, HttpStatus status, boolean error, String msg) {
        this.data = data;
        this.status = status;
        this.error = error;
        this.msg = msg;
    }
}
