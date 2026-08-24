package com.example.inventory.controller;

import com.example.inventory.exception.DuplicateProductException;
import com.example.inventory.exception.EmptyInventoryException;
import com.example.inventory.exception.InsufficientStockException;
import com.example.inventory.exception.InvalidProductDataException;
import com.example.inventory.exception.ProductNotFoundException;
import com.example.inventory.model.Product;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    // In-memory temporary storage, as required by the scenario
    private final List<Product> productList = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    // CREATE
    @PostMapping
    public ResponseEntity<Product> addProduct(@Valid @RequestBody Product product) {
        if (product.getProductId() != null) {
            boolean exists = productList.stream()
                    .anyMatch(p -> p.getProductId().equals(product.getProductId()));
            if (exists) {
                throw new DuplicateProductException(product.getProductId());
            }
        } else {
            product.setProductId(idCounter.getAndIncrement());
        }
        productList.add(product);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    // READ - all
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        if (productList.isEmpty()) {
            throw new EmptyInventoryException("Inventory is currently empty");
        }
        return ResponseEntity.ok(productList);
    }

    // READ - by id
    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable Long productId) {
        Product product = findProductOrThrow(productId);
        return ResponseEntity.ok(product);
    }

    // READ - by category
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable String category) {
        if (category == null || category.isBlank()) {
            throw new InvalidProductDataException("Category filter cannot be blank");
        }
        List<Product> filtered = productList.stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .toList();
        if (filtered.isEmpty()) {
            throw new ProductNotFoundException(-1L);
        }
        return ResponseEntity.ok(filtered);
    }

    // UPDATE - full update
    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long productId, @Valid @RequestBody Product updatedProduct) {
        Product existing = findProductOrThrow(productId);
        existing.setProductName(updatedProduct.getProductName());
        existing.setCategory(updatedProduct.getCategory());
        existing.setStock(updatedProduct.getStock());
        existing.setPrice(updatedProduct.getPrice());
        return ResponseEntity.ok(existing);
    }

    // UPDATE - reduce stock (e.g. after a sale/dispatch)
    @PatchMapping("/{productId}/reduce-stock")
    public ResponseEntity<Product> reduceStock(
            @PathVariable Long productId, @RequestParam int quantity) {
        Product existing = findProductOrThrow(productId);
        if (quantity <= 0) {
            throw new InvalidProductDataException("Quantity to reduce must be greater than zero");
        }
        if (existing.getStock() < quantity) {
            throw new InsufficientStockException(productId, quantity, existing.getStock());
        }
        existing.setStock(existing.getStock() - quantity);
        return ResponseEntity.ok(existing);
    }

    // DELETE
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        Product existing = findProductOrThrow(productId);
        productList.remove(existing);
        return ResponseEntity.noContent().build();
    }

    private Product findProductOrThrow(Long productId) {
        return productList.stream()
                .filter(p -> p.getProductId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }
}
