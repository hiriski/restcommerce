package id.neuman.catalog.controllers;

import id.neuman.catalog.entities.Product;
import id.neuman.catalog.services.CatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CatalogController {

    private final CatalogService catalogService;

    @GetMapping("/{id}")
    ResponseEntity<Product> findProductById(@PathVariable("id") String id) {
        return ResponseEntity.ok(catalogService.findProductById(id));
    }
}