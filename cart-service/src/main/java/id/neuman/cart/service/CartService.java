package id.neuman.cart.service;

import id.neuman.cart.dto.request.AddEditToCartRequest;
import id.neuman.cart.entities.Cart;
import id.neuman.cart.repositories.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;

    public String addEditToCart(String action, AddEditToCartRequest request) {

        if (!(action.equalsIgnoreCase("add") ||
                action.equalsIgnoreCase("reduce") ||
                action.equalsIgnoreCase("edit"))) {
            return "Wrong action!";
        }

        Cart cart = cartRepository.findByProductId(request.getProductId()).orElse(new Cart());

        Integer quantity =  editQuantityProduct(action, cart.getQuantity());

        if (action.equalsIgnoreCase("edit")) {
            quantity = request.getQuantity();
        }

        BeanUtils.copyProperties(request, cart);
        cart.setQuantity(quantity);
        cartRepository.save(cart);

        return "Success";
    }

    private Integer editQuantityProduct(String action, Integer quantity) {
        quantity = quantity == null ? 0 : quantity;
        return action.equalsIgnoreCase("add") ? quantity + 1 : quantity - 1;
    }
}