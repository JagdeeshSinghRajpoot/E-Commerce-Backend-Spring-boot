package com.shop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.shop.entitys.Image;
import com.shop.entitys.Product;
import com.shop.repository.ImageRepository;
import com.shop.repository.ProductRepository;
import com.shop.service.FileService;
import com.shop.service.ImageService;

import java.util.*;
import java.io.IOException;

@Service
public class ImageServiceImpl implements ImageService {

    // private final String UPLOAD_DIR = "src/main/resources/static/image/";
    // Directory for storing uploaded photos

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private FileService fileService;

    // Retrieve photos by product ID
    public List<Image> getPhotosByProductId(Long productId) {
        return imageRepository.findByProductId(productId);
    }

    @Override
    public void deleteImage(Long imageId) throws IOException {
        // Find the image by its ID in the repository
        Optional<Image> optionalImage = imageRepository.findById(imageId);

        if (optionalImage.isEmpty()) {
            throw new RuntimeException("Image not found");
        }

        Image image = optionalImage.get();
        String fileName = image.getUrl();

        // Remove the image file from the file system
        if (fileService.deleteImage(fileName)) {
            // Remove the image entity from the database
            imageRepository.delete(image);
        }
    }

    public List<Image> getAllImages() {
        return imageRepository.findAll();
    }

}
