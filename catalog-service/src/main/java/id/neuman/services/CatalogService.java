package id.neuman.services;

import id.neuman.dto.request.CreateProductRequest;
import id.neuman.entities.Product;
import id.neuman.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CatalogService {

    private final ProductRepository productRepository;

    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    public Product findProductById(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found."));
    }

    public Product createProduct(CreateProductRequest request) {
        Product product = new Product();
        BeanUtils.copyProperties(request, product);

        return productRepository.save(product);
    }
}