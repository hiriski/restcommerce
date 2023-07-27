package id.neuman.gateway.errorhandling;

import id.neuman.common.dto.response.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;

@RestControllerAdvice
public class ApiGatewayRestControllerAdvice {

    @ExceptionHandler(value = RestClientException.class)
    public ResponseEntity<Object> handleRestClientException(RestClientException e) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new BaseResponse<>(e.getMessage(), null));
    }
}