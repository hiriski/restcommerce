package id.neuman.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private final RestTemplate restTemplate;

    private final RouteValidator routeValidator;


    public AuthenticationFilter(RestTemplate restTemplate, RouteValidator routeValidator) {
        super(Config.class);
        this.restTemplate = restTemplate;
        this.routeValidator = routeValidator;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            if (routeValidator.isAllowedEndpoint.test(request)) {
                // check if header not contains token
                List<String> authHeaders = request.getHeaders().get(HttpHeaders.AUTHORIZATION);
                if (authHeaders == null) {
                    throw new RuntimeException("Missing Authorization header!");
                }

                HttpEntity<String> entity = new HttpEntity<>(null, request.getHeaders());

                restTemplate
                        .postForObject("http://identity-service/auth/validate-token", entity, Boolean.class);
            }

            return chain.filter(exchange);
        });
    }

    public static class Config {
    }
}
