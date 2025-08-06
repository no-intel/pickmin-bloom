package org.noint.pickminbloom.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {PickminBloomException.class})
    public ResponseEntity<Object> exceptionResponse(PickminBloomException e) {
        log.error("ExceptionHandler PickminBloomException - : ", e);
        HashMap<String, Object> body = new HashMap<>();
        body.put("code", e.getCode());
        body.put("field", e.getField());
        body.put("message", e.getMessage());
        return ResponseEntity.status(e.getCode()).body(body);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        HashMap<String, Object> body = new HashMap<>();
        body.put("code", status.value());
        body.put("field", ex.getFieldError().getField());
        body.put("message", Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage());
        return ResponseEntity.status(status).body(body);
    }
}