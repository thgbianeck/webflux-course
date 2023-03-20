package br.com.bieniek.webfluxcourse.controller.exceptions;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.time.LocalDateTime;

@Data
@Builder
public class StandardError {

    @Serial
    private static final long serialVersionUID = 1L;

    private LocalDateTime timestamp;
    private String path;
    private Integer status;
    private String error;
    private String message;
}
