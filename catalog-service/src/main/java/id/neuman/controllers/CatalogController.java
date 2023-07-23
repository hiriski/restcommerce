package id.neuman.controllers;

import id.neuman.dto.request.CreateProductRequest;
import id.neuman.dto.response.BaseResponse;
import id.neuman.entities.Product;
import id.neuman.services.CatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CatalogController {

    private final CatalogService catalogService;

    @GetMapping
    BaseResponse<List<Product>> findAllProducts() {
        return new BaseResponse<>("Success", catalogService.findAllProducts());
    }

    @GetMapping("/{id}")
    BaseResponse<Product> findProductById(@PathVariable("id") String id) {
        return new BaseResponse<>("Success", catalogService.findProductById(id));
    }

    @PostMapping
    BaseResponse<Product> createProduct(@RequestBody CreateProductRequest request) {
        return new BaseResponse<>("Created", catalogService.createProduct(request));
    }
}
