package id.neuman.gateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.neuman.common.dto.response.BaseResponse;
import id.neuman.gateway.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Slf4j
@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private final ObjectMapper objectMapper;

    private final RestTemplate restTemplate;

    private final RouteValidator routeValidator;

    public AuthenticationFilter(ObjectMapper objectMapper, RestTemplate restTemplate, RouteValidator routeValidator) {
        super(Config.class);
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
        this.routeValidator = routeValidator;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return new OrderedGatewayFilter((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            if (routeValidator.isAllowedEndpoint.test(request)) {
                log.info("INSIDE AUTH FILTER");

                ServerHttpRequest serverHttpRequest = exchange.getRequest();

                try {
                    ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                            "http://identity-service/auth/validate-token",
                            new HttpEntity<>(null, serverHttpRequest.getHeaders()),
                            String.class
                    );

                    BaseResponse<Boolean> baseResponse = objectMapper.readValue(responseEntity.getBody(), BaseResponse.class);
                    if (baseResponse.getData().equals(Boolean.FALSE)) {
                        throw new ApiException(UNAUTHORIZED, baseResponse.getMessage());
                    }
                } catch (HttpClientErrorException e) {
                    throw new ApiException(UNAUTHORIZED, e.getResponseBodyAsString());
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }

            return chain.filter(exchange);
        }, 0);
    }

    public static class Config {
    }
}
