package com.example.personal_project.presentation;

import com.example.personal_project.domain.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //도메인 객체
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorMessage> handleConstraintViolationException(
            ConstraintViolationException ex
    ){
        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
        List<String> errors = constraintViolations.stream()
                .map(constraintViolation -> extractField(constraintViolation.getPropertyPath()) + ", " + constraintViolation.getMessage())
                .toList();

        ErrorMessage errorMessage = new ErrorMessage(errors);
        return new ResponseEntity(errorMessage,HttpStatus.BAD_REQUEST);
    }

    //Controller DTO
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex
    ){
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        List<String> errors = fieldErrors.stream()
                .map(fieldError -> fieldError.getField() + ", " + fieldError.getDefaultMessage())
                .toList();
        ErrorMessage errorMessage = new ErrorMessage(errors);
        return new ResponseEntity(errorMessage, HttpStatus.BAD_REQUEST);
    }

    //Controller queryParam
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErrorMessage> handleHandlerMethodValidationException(
            HandlerMethodValidationException ex
    ) {
        List<String> messages = ex.getAllErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .toList();
        ErrorMessage errors = new ErrorMessage(messages);
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleEntityNotFoundException(
            EntityNotFoundException ex
    ){
        List<String> list = new ArrayList<>();
        list.add(ex.getMessage());
        ErrorMessage errors = new ErrorMessage(list);
        return new ResponseEntity(errors,HttpStatus.NOT_FOUND);
    }

    private String extractField(Path path){
        String[] splittedArray = path.toString().split("[.]");
        int lastIndex = splittedArray.length -1;
        return splittedArray[lastIndex];
    }
}
