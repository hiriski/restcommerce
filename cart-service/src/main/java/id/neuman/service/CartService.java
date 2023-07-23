package id.neuman.service;

import id.neuman.dto.request.AddEditToCartRequest;
import id.neuman.entities.Cart;
import id.neuman.repositories.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;

    public Object addEditToCart(String action, AddEditToCartRequest request) {

        if (request.getCartId() != null && action.equalsIgnoreCase("add")) {

            Cart cart = cartRepository.findById(request.getCartId())
                    .orElseThrow(() -> new IllegalArgumentException("Cart id not exists!"));

            Integer quantity = cart.getQuantity();
            quantity += 1;

            cart.setQuantity(quantity);

            cartRepository.save(cart);
        } else if (request.getCartId() != null && action.equalsIgnoreCase("edit")) {
            Cart cart = cartRepository.findById(request.getCartId())
                    .orElseThrow(() -> new IllegalArgumentException("Cart id not exists!"));

            Integer quantity = cart.getQuantity();
            quantity -= 1;

            cart.setQuantity(quantity);

            cartRepository.save(cart);
            return "Success";
        }

        Cart cart = new Cart();
        BeanUtils.copyProperties(request, cart);

        cartRepository.save(cart);

        return "Success";
    }
}