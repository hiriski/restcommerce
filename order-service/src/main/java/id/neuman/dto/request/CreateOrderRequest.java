package id.neuman.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {
    private Long userId;
    private String username;
    private String productId;
    private String productName;
    private String productDesc;
    private BigDecimal productPrice;
    private Integer quantity;
    private Long paymentId;
}