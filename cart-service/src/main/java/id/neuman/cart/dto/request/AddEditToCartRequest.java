package id.neuman.cart.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddEditToCartRequest {
    private UUID userId;
    private String productId;
    private Integer quantity;
}