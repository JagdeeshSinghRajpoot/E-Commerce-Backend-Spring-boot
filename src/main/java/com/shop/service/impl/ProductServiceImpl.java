package com.shop.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.shop.entitys.Image;
import com.shop.entitys.Product;
import com.shop.entitys.Subcategory;
import com.shop.repository.ImageRepository;
import com.shop.repository.ProductRepository;
import com.shop.repository.SubCategoryRepository;
import com.shop.service.FileService;
import com.shop.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Optional<Product> getProductById(Long Id) {
        return productRepository.findById(Id);
    }
 
    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll(Sort.by(Sort.Direction.DESC, "productListDate"));
    }

    @Override
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    // delete image for a product
    public void deleteProductImage(Long productId, Long imageId) throws IOException {
        // Find the product by its ID
        Optional<Product> productOptional = productRepository.findById(productId);

        if (productOptional.isPresent()) {
            Product product = productOptional.get();

            // Find the image to be deleted
            Optional<Image> imageToDelete = product.getImages().stream()
                    .filter(image -> image.getId().equals(imageId))
                    .findFirst();

            if (imageToDelete.isPresent()) {
                // Remove the image from the product's image set
                fileService.deleteImage(imageToDelete.get().getUrl());
                product.getImages().remove(imageToDelete.get());
                // Save the product after removing the image
                productRepository.save(product);
            } else {
                throw new RuntimeException("Image not found with id: " + imageId);
            }
        } else {
            throw new RuntimeException("Product not found with id: " + productId);
        }
    }

    @Override
    public Product updateProduct(Long id, Product updatedProduct) {
        Optional<Product> existingProduct = productRepository.findById(id);
        if (existingProduct.isPresent()) {
            Product product = existingProduct.get();
            product.setName(updatedProduct.getName());
            product.setDescription(updatedProduct.getDescription());
            product.setPrice(updatedProduct.getPrice());
            product.setCompany(updatedProduct.getCompany());
            product.setTitle(updatedProduct.getTitle());
            product.setColour(updatedProduct.getColour());
            product.setRating(updatedProduct.getRating());
            product.setSeller(updatedProduct.getSeller());
            product.setQuantity(updatedProduct.getQuantity());
            return productRepository.save(product);
        } else {
            return null; // Or handle exception (e.g., throw ProductNotFoundException)
        }
    }

    @Override
    public Product AddSubCategory(Long productId, Long subCategoryId) {
        Optional<Product> existingProduct = productRepository.findById(productId);
        Optional<Subcategory> existingSubCategory = subCategoryRepository.findById(subCategoryId);

        if (existingProduct.isPresent() && existingSubCategory.isPresent()) {
            Product product = existingProduct.get();

            Subcategory subcategory = existingSubCategory.get();

            product.setSubcategory(subcategory);
            return productRepository.save(product);
        } else {
            return null; // Or handle exception (e.g., throw ProductNotFoundException)
        }
    }

    // ==============================image service

    // Store multiple photos and link them to a product
    public List<Image> uploadImages(Long productId, MultipartFile[] files) throws IOException {
        Optional<Product> optionalProduct = productRepository.findById(productId);

        if (optionalProduct.isEmpty()) {
            throw new RuntimeException("Product not found");
        }

        Product product = optionalProduct.get();
        List<Image> savedPhotos = new ArrayList<>();

        // Iterate over each uploaded file and save it
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {

                String fileName = fileService.uploadImage(file);

                // Create a new Photo entity
                Image photo = new Image();
                photo.setUrl(fileName);
                photo.setProduct(product);


                // Save the photo entity
                Image savedPhoto = imageRepository.save(photo);
                savedPhotos.add(savedPhoto);
            }
        }
        return savedPhotos;
    }

}
