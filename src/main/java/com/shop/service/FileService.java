package com.shop.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface FileService {
    String uploadImage(MultipartFile file) throws IOException;

    boolean deleteImage(String fileName) throws IOException;

    InputStream getResource(String path, String fileName) throws FileNotFoundException;
}
