package id.neuman.cart.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import id.neuman.cart.dto.request.AddEditToCartRequest;
import id.neuman.cart.service.CartService;
import id.neuman.common.dto.response.BaseResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CartController {

	private final CartService cartService;

	@PostMapping("/add-edit-to-cart")
	BaseResponse<String> addEditToCart(@RequestParam(name = "action") String action,
			@RequestBody AddEditToCartRequest request) {
		return new BaseResponse<>(cartService.addEditToCart(action, request), null);
	}
}