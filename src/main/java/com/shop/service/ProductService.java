package com.shop.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.shop.entitys.Image;
import com.shop.entitys.Product;

@Service
public interface ProductService {

    Product createProduct(Product product);

    Optional<Product> getProductById(Long Id);

    // Optional<Product> getProductById(Long id);
    List<Product> getAllProducts();

    void deleteProduct(Long id);

    Product updateProduct(Long id, Product product);

    Product AddSubCategory(Long productId, Long subCategoryId);

    void deleteProductImage(Long productId, Long imageId) throws IOException;

    List<Image> uploadImages(Long productId, MultipartFile[] files) throws IOException;

}
