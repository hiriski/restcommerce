package id.neuman.controllers;

import id.neuman.dto.request.AddEditToCartRequest;
import id.neuman.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/carts")
public class CartController {

    private final CartService cartService;

    @PostMapping("/add-edit-to-cart")
    Object addEditToCart(@RequestParam(name = "action") String action, @RequestBody AddEditToCartRequest request) {
        return cartService.addEditToCart(action, request);
    }
}