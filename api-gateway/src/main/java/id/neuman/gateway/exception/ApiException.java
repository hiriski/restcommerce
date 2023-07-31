package id.neuman.gateway.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpClientErrorException;

@Getter
@Setter
public class ApiException extends HttpClientErrorException {

    private final String message;

    public ApiException(HttpStatusCode statusCode, String msg) {
        super(statusCode, msg);
        this.message = msg;
    }
}