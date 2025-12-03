package com.Etac.John.Llyod.WS101_Act7.service;

import com.Etac.John.Llyod.WS101_Act7.model.Product;
import com.Etac.John.Llyod.WS101_Act7.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductService {
    
    @Autowired
    private ProductRepository productRepository;
    
    // Get all products
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    
    // Get product by ID
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }
    
    // Create new product
    public Product createProduct(Product product) {
        validateProduct(product);
        return productRepository.save(product);
    }
    
    // Update existing product
    public Optional<Product> updateProduct(Long id, Product productDetails) {
        return productRepository.findById(id)
            .map(existingProduct -> {
                if (productDetails.getName() != null && !productDetails.getName().trim().isEmpty()) {
                    existingProduct.setName(productDetails.getName());
                }
                if (productDetails.getPrice() != null && productDetails.getPrice() >= 0) {
                    existingProduct.setPrice(productDetails.getPrice());
                }
                return productRepository.save(existingProduct);
            });
    }
    
    // Delete product
    public boolean deleteProduct(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    // Search products by name
    public List<Product> searchProductsByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return getAllProducts();
        }
        return productRepository.findByNameContainingIgnoreCase(name);
    }
    
    // Search products by price range
    public List<Product> searchProductsByPriceRange(Double minPrice, Double maxPrice) {
        if (minPrice != null && maxPrice != null) {
            return productRepository.findByPriceRange(minPrice, maxPrice);
        } else if (minPrice != null) {
            return productRepository.findByPriceGreaterThanEqual(minPrice);
        } else if (maxPrice != null) {
            return productRepository.findByPriceLessThanEqual(maxPrice);
        }
        return getAllProducts();
    }
    
    // Validate product data
    private void validateProduct(Product product) {
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }
        if (product.getPrice() == null || product.getPrice() < 0) {
            throw new IllegalArgumentException("Product price must be non-negative");
        }
        if (product.getPrice() > 1000000) {
            throw new IllegalArgumentException("Product price cannot exceed 1,000,000");
        }
    }
}