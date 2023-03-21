package br.com.bieniek.webfluxcourse.controller.exceptions;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(DuplicateKeyException.class)
    ResponseEntity<Mono<StandardError>> duplicateKeyException(DuplicateKeyException ex, ServerHttpRequest request) {
        return ResponseEntity.badRequest()
                .body(Mono.just(
                        StandardError.builder()
                                .timestamp(now())
                                .status(BAD_REQUEST.value())
                                .error(BAD_REQUEST.getReasonPhrase())
                                .message(verifyDupKey(ex.getMessage()))
                                .path(request.getPath().toString())
                                .build()
                        )
                );
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<Mono<ValidationError>> validationError(WebExchangeBindException ex, ServerHttpRequest request) {

        ValidationError error = new ValidationError(now(), request.getPath().toString(), BAD_REQUEST.value(),
                "Validation error", "Error on validations attributes");

        ex.getBindingResult().getFieldErrors().forEach(x ->error.addError(x.getField(), x.getDefaultMessage()));

        return ResponseEntity.badRequest().body(Mono.just(error));

    }

    private String verifyDupKey(String message) {
        if (message.contains("email dup key")) {
            return "Email already exists";
        }
        return "Dup key exception";
    }
}
