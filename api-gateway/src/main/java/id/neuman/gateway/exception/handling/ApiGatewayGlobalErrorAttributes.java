package id.neuman.gateway.exception.handling;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.neuman.common.dto.response.BaseResponse;
import id.neuman.gateway.exception.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApiGatewayGlobalErrorAttributes extends DefaultErrorAttributes {

    private final ObjectMapper objectMapper;

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Throwable error = this.getError(request);
        log.error("Error occurred ", error);
        MergedAnnotation<ResponseStatus> responseStatusAnnotation = MergedAnnotations
                .from(error.getClass(), MergedAnnotations.SearchStrategy.TYPE_HIERARCHY).get(ResponseStatus.class);
        HttpStatusCode errorStatusCode = findHttpStatusCode(error, responseStatusAnnotation);
        log.info("errorStatus: {}", errorStatusCode);
        Map<String, Object> map = super.getErrorAttributes(request, options);
        String errorCode = getErrorCode(map, errorStatusCode);
        map.put("error", errorCode);
        map.put("status", errorStatusCode.value());

        try {
            map.put("message", objectMapper.readValue(error.getMessage(), BaseResponse.class));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        log.info("ERROR MESSAGE: {}", error.getMessage());

        return map;
    }

    private HttpStatusCode findHttpStatusCode(Throwable error, MergedAnnotation<ResponseStatus> responseStatusAnnotation) {
        if (error instanceof ResponseStatusException responseStatusException) {
            return responseStatusException.getStatusCode();
        } else if (error instanceof ApiException apiException) {
            return apiException.getStatusCode();
        }
        return responseStatusAnnotation.getValue("code", HttpStatus.class).orElse(INTERNAL_SERVER_ERROR);
    }

    private String getErrorCode(Map<String, Object> map, HttpStatusCode errorStatusCode) {
        String errorCode;
        if (errorStatusCode.value() == UNAUTHORIZED.value()) {
            errorCode = "401 Unauthorized";
        } else {
            if (errorStatusCode.value() == NOT_FOUND.value()) {
                log.error("The url:{} is not found", map.get("path"));
                errorCode = "404 Not Found";
            } else if (errorStatusCode.value() == METHOD_NOT_ALLOWED.value()) {
                log.error("Invalid HTTP Method type for the url: {}", map.get("path"));
                errorCode = "405 Method Not Allowed";
            } else {
                log.error("Unexpected error happened");
                log.error("error status is : {}", errorStatusCode);
                errorCode = "500 Internal Server Error";
            }
        }
        return errorCode;
    }
}