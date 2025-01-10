package com.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.shop.entitys.Image;
import com.shop.service.ImageService;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping("/image")
public class ImageController {

    @Autowired
    private ImageService imageService;

    // Endpoint to upload multiple photos for a product
    // @PostMapping("product/{productId}")
    // public ResponseEntity<List<Image>> uploadImage(@PathVariable("productId") Long productId,
    //         @RequestParam("files") MultipartFile[] files) {
    //     try {
    //         List<Image> savedPhotos = imageService.uploadImages(productId, files);
    //         return ResponseEntity.ok(savedPhotos);
    //     } catch (IOException e) {
    //         return ResponseEntity.status(500).body(null); // Handle the exception
    //     }
    // }

    // =================================================================================
    private static final String IMAGE_UPLOAD_DIR = "src/main/resources/static/image/";

    // Endpoint to download multiple images as a ZIP file
    @GetMapping("/downloadMultiple")
    public ResponseEntity<InputStreamResource> downloadMultipleImages(
            @RequestParam List<String> fileNames) throws IOException {

        // Create a ByteArrayOutputStream to hold the ZIP data
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);

        // Loop through the list of file names and add them to the ZIP
        for (String fileName : fileNames) {
            Path imagePath = Paths.get(IMAGE_UPLOAD_DIR).resolve(fileName).normalize();
            File file = imagePath.toFile();

            // Check if file exists
            if (file.exists()) {
                // Create a new ZipEntry for each file and write it to the ZipOutputStream
                zipOutputStream.putNextEntry(new ZipEntry(fileName));
                FileInputStream fileInputStream = new FileInputStream(file);

                // Write the file's bytes to the zip
                byte[] bytes = new byte[1024];
                int length;
                while ((length = fileInputStream.read(bytes)) >= 0) {
                    zipOutputStream.write(bytes, 0, length);
                }

                // Close the current entry in the ZIP
                zipOutputStream.closeEntry();
                fileInputStream.close();
            } else {
                return ResponseEntity.badRequest().body(null);
            }
        }

        // Close the ZipOutputStream
        zipOutputStream.finish();
        zipOutputStream.close();

        // Return the ZIP file as a downloadable response
        InputStreamResource resource = new InputStreamResource(
                new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=images.zip")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
    // ============================================================================================

    @GetMapping("/{fileName}")
    public ResponseEntity<UrlResource> getImage(@PathVariable("fileName") String fileName)
            throws MalformedURLException {
        try {
            // Path to the image file
            Path imagePath = Paths.get(IMAGE_UPLOAD_DIR).resolve(fileName).normalize();
            UrlResource resource = new UrlResource(imagePath.toUri());

            if (resource.exists()) {
                // Get content type
                String contentType = Files.probeContentType(imagePath);
                if (contentType == null) {
                    contentType = "application/octet-stream"; // Fallback if unknown
                }

                // Return the image file as a response entity
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            return ResponseEntity.status(500).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable("id") Long id) {
        try {
            imageService.deleteImage(id);
            return ResponseEntity.noContent().build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // get all images
    @GetMapping
    public ResponseEntity<List<Image>> getAllImages() {
        return ResponseEntity.ok(imageService.getAllImages());
    }

}
