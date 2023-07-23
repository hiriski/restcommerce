package id.neuman.repositories;

import id.neuman.dto.response.PaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class PaymentRepository {

    private final RestTemplate restTemplate;

    public PaymentResponse findById(Long id) {
        ResponseEntity<PaymentResponse> responseEntity = restTemplate
                .getForEntity("http://localhost:8080/payments/" + id, PaymentResponse.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return responseEntity.getBody();
        }

        throw new IllegalArgumentException("Problem when find payment method");
    }
}