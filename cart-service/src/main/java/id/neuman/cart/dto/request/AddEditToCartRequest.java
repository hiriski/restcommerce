package id.neuman.cart.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddEditToCartRequest {
    private Long cartId;
    private Long userId;
    private String productId;
    private Integer quantity;
}