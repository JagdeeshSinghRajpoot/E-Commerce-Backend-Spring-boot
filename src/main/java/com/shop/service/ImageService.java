package com.shop.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.shop.entitys.Image;

@Service
public interface ImageService {

    List<Image> getPhotosByProductId(Long productId);

    void deleteImage(Long imageId) throws IOException;

    List<Image> getAllImages();
}
