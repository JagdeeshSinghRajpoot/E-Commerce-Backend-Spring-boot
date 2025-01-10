package com.shop.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import com.shop.service.FileService;

@Service
public class FileServiceImpl implements FileService {

    private final String UPLOAD_DIR = "src/main/resources/static/image/"; // Directory for storing uploaded photos

    @Override
    public String uploadImage(MultipartFile file) throws IOException {

        // File Name
        String name = file.getOriginalFilename();
        // abc.png
        // random name generate file
        String randomID = UUID.randomUUID().toString();
        String fileName = randomID.concat(name.substring(name.lastIndexOf(".")));
        // fullpath
        String filePath = UPLOAD_DIR + fileName;
        // create folder if not created
        File f = new File(UPLOAD_DIR);
        if ((!f.exists())) {
            f.mkdir();
        }
        // file copy
        Files.copy(file.getInputStream(), Paths.get(filePath));
        return fileName;
    }

    public boolean deleteImage(String fileName) throws IOException {

        Path filePath = Paths.get(UPLOAD_DIR + fileName);
        if (Files.exists(filePath)) {
            Files.delete(filePath);
        } else {
            throw new RuntimeException("File not found: " + fileName);
        }
        return true;
    }

    @Override
    public InputStream getResource(String path, String fileName) throws FileNotFoundException {
        String fullPath = path + File.separator + fileName;
        InputStream is = new FileInputStream(fullPath);
        // db logic to return inputstream
        return is;
    }

}
