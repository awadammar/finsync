package com.project.finsync.exception;

import com.project.finsync.util.ResponseBody;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex) {
        ResponseBody responseBody = ResponseBody.builder().httpStatus(HttpStatus.BAD_REQUEST).message(ex.getMessage()).build();
        return buildResponseEntity(responseBody);
    }

    @ExceptionHandler({DuplicateEntityException.class, DataIntegrityViolationException.class})
    protected ResponseEntity<Object> handleDuplicateEntity(Exception ex) {
        ResponseBody responseBody = ResponseBody.builder().httpStatus(HttpStatus.BAD_REQUEST).message(ex.getMessage()).build();
        return buildResponseEntity(responseBody);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();
        ResponseBody responseBody = ResponseBody.builder().httpStatus(HttpStatus.BAD_REQUEST).message("Validation errors").data(errors).build();
        return buildResponseEntity(responseBody);
    }

    private ResponseEntity<Object> buildResponseEntity(ResponseBody responseBody) {
        return new ResponseEntity<>(responseBody, responseBody.getHttpStatus());
    }
}
