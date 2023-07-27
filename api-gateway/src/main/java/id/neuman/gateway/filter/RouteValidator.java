package id.neuman.gateway.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    public static final List<String> allowedEndpoint = List.of(
            "/auth/register",
            "/auth/login",
            "/validate-token",
            "/eureka"
    );

    protected Predicate<ServerHttpRequest> isAllowedEndpoint = request -> allowedEndpoint.stream()
            .noneMatch(uri -> request.getURI().getPath().contains(uri));
}