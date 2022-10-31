package fr.ramatellier.clonewar.util;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(IOException.class)
    ResponseEntity<Void> fileNotFound(){
        return ResponseEntity.notFound().build();
    }
}
