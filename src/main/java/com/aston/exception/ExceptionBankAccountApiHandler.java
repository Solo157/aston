package com.aston.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class ExceptionBankAccountApiHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorMessage> handleBankAccountApiException(RuntimeException exception) {
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(new ErrorMessage(
                        BAD_REQUEST.value(),
                        BAD_REQUEST.getReasonPhrase(),
                        exception.getMessage())
                );
    }

    @AllArgsConstructor
    static class ErrorMessage {
        @JsonProperty("error_code") Integer code;
        @JsonProperty("error_status") String status;
        @JsonProperty("error_description") String message;
    }
}
