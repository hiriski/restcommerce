package id.neuman.gateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.neuman.common.dto.response.BaseResponse;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Slf4j
@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private final ObjectMapper objectMapper;

    private final RouteValidator routeValidator;

    private final WebClient.Builder webClientBuilder;

    public AuthenticationFilter(ObjectMapper objectMapper, WebClient.Builder webClientBuilder, RouteValidator routeValidator) {
        super(Config.class);
        this.objectMapper = objectMapper;
        this.webClientBuilder = webClientBuilder;
        this.routeValidator = routeValidator;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return new OrderedGatewayFilter((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            log.info("**************************************************************************");
            log.info("URL: {}", request.getURI().getPath());
            String bearerToken = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            log.info("Bearer token empty?: {}", bearerToken != null && bearerToken.isEmpty());
            if (routeValidator.isAllowedEndpoint.test(request)) {
                return webClientBuilder.build().post()
                        .uri("lb://identity-service/auth/validate-token")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken)
                        .retrieve()
                        .bodyToMono(BaseResponse.class)
                        .map(response -> exchange)
                        .flatMap(chain::filter)
                        .onErrorResume(error -> {
                            HttpStatusCode errorCode;
                            String errorMsg;
                            if (error instanceof WebClientResponseException webClientException) {
                                errorCode = webClientException.getStatusCode();
                                errorMsg = Objects.requireNonNull(webClientException.getResponseBodyAs(BaseResponse.class)).getMessage();
                            } else {
                                errorCode = HttpStatus.BAD_GATEWAY;
                                errorMsg = HttpStatus.BAD_GATEWAY.getReasonPhrase();
                            }
                            log.info("Error happened: {}", errorMsg);

                            return onError(exchange, errorMsg, errorCode);
                        });
            }

            return chain.filter(exchange);
        }, 0);
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatusCode httpStatusCode) {
        DataBufferFactory dataBufferFactory = exchange.getResponse().bufferFactory();
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatusCode);
        try {
            response.getHeaders().add("Content-Type", "application/json");
            BaseResponse<String> data = new BaseResponse<>(err, null);
            byte[] byteData = objectMapper.writeValueAsBytes(data);
            return response.writeWith(Mono.just(byteData).map(dataBufferFactory::wrap));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return response.setComplete();
    }

    @NoArgsConstructor
    public static class Config {
    }
}
