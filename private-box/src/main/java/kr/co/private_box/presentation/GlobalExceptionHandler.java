package kr.co.private_box.presentation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import kr.co.private_box.domain.exception.GoneException;
import kr.co.private_box.domain.exception.LackOfPrivateUrlResource;
import kr.co.private_box.domain.exception.NotFoundException;
import kr.co.private_box.domain.exception.UnAuthorizedException;
import kr.co.private_box.presentation.dto.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.ArrayList;
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
        return new ResponseEntity(errorMessage, HttpStatus.BAD_REQUEST);
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

    @ExceptionHandler(GoneException.class)
    public ResponseEntity<ErrorMessage> handleGoneException(
            GoneException ex
    ){
        ErrorMessage errorMessage = getErrors(ex.getMessage());
        return new ResponseEntity<>(errorMessage,HttpStatus.GONE);
    }

    @ExceptionHandler(LackOfPrivateUrlResource.class)
    public ResponseEntity<ErrorMessage> handleLackOfPrivateUrlResource(
            LackOfPrivateUrlResource ex
    ){
        //개발자에게 메일이나 연락이 가도록 하는 코드??
        ErrorMessage errorMessage = getErrors(ex.getMessage());
        return new ResponseEntity<>(errorMessage,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorMessage> handleNotFoundException(
            NotFoundException ex
    ){
        ErrorMessage errorMessage = getErrors(ex.getMessage());
        return new ResponseEntity<>(errorMessage,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnAuthorizedException.class)
    public ResponseEntity<ErrorMessage> handleUnAuthorizedException(
            UnAuthorizedException ex
    ){
        ErrorMessage errorMessage = getErrors(ex.getMessage());
        return new ResponseEntity<>(errorMessage,HttpStatus.UNAUTHORIZED);
    }

    private ErrorMessage getErrors(String ex) {
        List<String> list = new ArrayList<>();
        list.add(ex);
        ErrorMessage errors = new ErrorMessage(list);
        return errors;
    }

    private String extractField(Path path){
        String[] splittedArray = path.toString().split("[.]");
        int lastIndex = splittedArray.length -1;
        return splittedArray[lastIndex];
    }
}
