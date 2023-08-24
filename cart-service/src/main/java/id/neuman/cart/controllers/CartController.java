package id.neuman.cart.controllers;

import id.neuman.cart.dto.request.AddEditToCartRequest;
import id.neuman.cart.service.CartService;
import id.neuman.common.dto.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/add-edit-to-cart")
    BaseResponse<String> addEditToCart(@RequestParam(name = "action") String action, @RequestBody AddEditToCartRequest request) {
        return new BaseResponse<>(cartService.addEditToCart(action, request), null);
    }
}