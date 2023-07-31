package id.neuman.identity.exception.handling;

import id.neuman.common.dto.response.BaseResponse;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestApiControllerAdvice {

    @ExceptionHandler(value = JwtException.class)
    public ResponseEntity<Object> handleJwtException(JwtException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new BaseResponse<>(e.getMessage(), null));
    }
}