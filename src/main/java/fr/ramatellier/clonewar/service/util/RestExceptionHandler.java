package fr.ramatellier.clonewar.service.util;

import fr.ramatellier.clonewar.exception.InvalidJarException;
import fr.ramatellier.clonewar.exception.InvalidJarFormatException;
import fr.ramatellier.clonewar.exception.PomNotFoundException;
import fr.ramatellier.clonewar.exception.PomNotSameException;
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

    @ExceptionHandler(InvalidJarException.class)
    ResponseEntity<Void> badJarInjected(){
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(PomNotFoundException.class)
    ResponseEntity<Void> pomNotFound(){
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(InvalidJarFormatException.class)
    ResponseEntity<Void> invalidJarContent(){
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(PomNotSameException.class)
    ResponseEntity<Void> pomNotSame(){
        return ResponseEntity.badRequest().build();
    }
}
