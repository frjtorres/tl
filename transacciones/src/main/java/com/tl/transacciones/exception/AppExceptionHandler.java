package com.tl.transacciones.exception;

import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.ServletWebRequest;

import javax.validation.ConstraintViolationException;
import java.util.*;
import java.util.stream.Collectors;

@RestControllerAdvice
public class AppExceptionHandler {

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(InvalidFormatException.class)
    public ExceptionTemplate invalidFormatExceptionHandler(InvalidFormatException ex, ServletWebRequest wr) {
        if(ex.getTargetType().isEnum()) {
            Optional<Reference> field = ex.getPath().stream()
                    .findFirst();

            if(field.isPresent()) {
                String reason = field.get().getFieldName() + ": Solo esta permitido los valores " +
                        Arrays.toString(ex.getTargetType().getEnumConstants()) + ".";

                return new ExceptionTemplate(HttpStatus.CONFLICT, wr, reason);
            }
        }

        return new ExceptionTemplate(HttpStatus.CONFLICT, wr);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ConstraintViolationException.class)
    public ExceptionTemplate constraintViolationExceptionHandler(ConstraintViolationException ex, ServletWebRequest wr) {
        List<String> reasons = ex.getConstraintViolations().stream()
                .map(a -> a.getPropertyPath() + ": " + a.getMessage())
                .sorted(Comparator.comparing(String::toString))
                .collect(Collectors.toList());

        return new ExceptionTemplate(HttpStatus.CONFLICT, wr, reasons);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(IllegalArgumentException.class)
    public ExceptionTemplate illegalArgumentExceptionHandler(IllegalArgumentException ex, ServletWebRequest wr) {
        return new ExceptionTemplate(HttpStatus.CONFLICT, wr, ex.getMessage());
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity httpClientErrorExceptionHandler(HttpClientErrorException ex, ServletWebRequest wr) {
        String message = new JacksonJsonParser().parseMap(ex.getResponseBodyAsString()).get("message").toString();
        ExceptionTemplate et = new ExceptionTemplate(ex.getStatusCode(), wr, message);

        return ResponseEntity.status(ex.getStatusCode()).body(et);
    }
}