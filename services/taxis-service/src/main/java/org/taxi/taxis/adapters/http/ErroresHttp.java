package org.taxi.taxis.adapters.http;

import org.taxi.taxis.domain.ErrorNegocio;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
@RestControllerAdvice
public class ErroresHttp {
    @ExceptionHandler(ErrorNegocio.class)
    ResponseEntity<?> negocio(ErrorNegocio error) {
        int status = switch(error.tipo()) {
            case NO_EXISTE -> 404;
            case CONFLICTO -> 409;
            case INVALIDO -> 400;
            case NO_DISPONIBLE -> 503;
        };
        return ResponseEntity.status(status).body(Map.of("error", error.getMessage()));
    }
    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    ResponseEntity<?> duplicado() { return ResponseEntity.status(409).body(Map.of("error", "El registro entra en conflicto con datos existentes")); }
}
