package com.data.service;

import com.data.entity.OrderItem;
import com.data.entity.Product;
import com.data.repository.OrderItemRepository;
import com.data.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private OrderItemRepository orderItemRepository;

    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product updatedProduct) {
        Product existingProduct = productRepository.findById(id).orElse(null);
        if (existingProduct != null) {
            existingProduct.setName(updatedProduct.getName());
            existingProduct.setDescription(updatedProduct.getDescription());
            existingProduct.setPrice(updatedProduct.getPrice());
            existingProduct.setAvailability(updatedProduct.isAvailability());
            existingProduct.setStockQuantity(updatedProduct.getStockQuantity());
            existingProduct.setImage(updatedProduct.getImage());
            
            return productRepository.save(existingProduct);
        }
        return null;
    }

    public void deleteProduct(Long id) {
    	Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Remove product reference from order items
        List<OrderItem> items = orderItemRepository.findByProductId(id);
        for (OrderItem item : items) {
            item.setProduct(null); // break the relation
            orderItemRepository.save(item);
        }

        productRepository.deleteById(id); // now safe to delete
    }


    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> searchProducts(String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword);
    }
    
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }
}
