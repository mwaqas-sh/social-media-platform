package com.waqas.social.media.platform.controller.advice;

import com.waqas.social.media.platform.utils.Constants;
import com.waqas.social.media.platform.utils.HttpRequestResult;
import com.waqas.social.media.platform.utils.Utilities;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GeneralExceptionHandler {

    public GeneralExceptionHandler() {
        super();
    }

    @ExceptionHandler(value = Throwable.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> defaultError(Throwable ex, HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();
        errors.put("Exception", ex.getMessage());
        return errors;
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<HttpRequestResult> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        if (null != ex) {

            ex.getBindingResult().getAllErrors().forEach(error -> {
                String fieldName = ((FieldError) error).getField();
                String errorMessage = error.getDefaultMessage();
                errors.put(fieldName, errorMessage);
            });
        }
        return Utilities.sendHttpBadRequestResponse(Constants.VALIDATION_ERROR, errors, "");
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<HttpRequestResult> constraintViolationException(
            ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        if (!ex.getConstraintViolations().isEmpty()) {
            for (ConstraintViolation<?> constraintViolation : ex.getConstraintViolations()) {
                String fieldName = null;
                for (Path.Node node : constraintViolation.getPropertyPath()) {
                    fieldName = node.getName();
                }
                errors.put(fieldName, constraintViolation.getMessage());
            }
        }
        return Utilities.sendHttpBadRequestResponse(Constants.CONSTRAINT_ERROR, errors, "");
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<HttpRequestResult> dataIntegrityViolationException(
            DataIntegrityViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("database exception", ex.getMostSpecificCause().getMessage());
        return Utilities.sendHttpBadRequestResponse(Constants.DATA_INTEGRITY_ERROR, errors, "");
    }

}