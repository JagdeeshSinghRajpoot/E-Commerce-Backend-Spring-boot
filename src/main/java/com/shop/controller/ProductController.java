package com.shop.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.shop.entitys.Image;
import com.shop.entitys.Product;
import com.shop.exception.ResourceNotFoundException;
import com.shop.service.ProductService;

import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/product")
@Validated
public class ProductController {

    @Autowired
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // create Product
    @PostMapping
    public ResponseEntity<String> createProduct(@Validated @RequestBody Product product) throws IOException {
        Product createdProduct = productService.createProduct(product);
        return new ResponseEntity<>("Product created successfully with Prduct id is - " + createdProduct.getId(),
                HttpStatus.CREATED);
    }

    // Get a product by ID
    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable("productId") Long productId) {
        Optional<Product> product = productService.getProductById(productId);
        System.out.println("The product id is : " + productId);
        return product.map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with Id: " + productId));
    }

    // Get all products
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    // put update product
    @PutMapping("/{id}")
    public ResponseEntity<String> putMethodName(@PathVariable("id") Long id, @RequestBody Product product) {
        Product updateProduct = productService.updateProduct(id, product);
        return new ResponseEntity<>("Product updated successfully with Prduct id is - " + updateProduct.getId(),
                HttpStatus.OK);
    }

     @PutMapping("/{prodid}/subcategory/{subid}")
     public ResponseEntity<Product> addSubCatetory(@PathVariable("prodid") Long
     prodid,
     @PathVariable("subid") Long subid) {
     Product updateProduct = productService.AddSubCategory(prodid, subid);
     return new ResponseEntity<>(updateProduct, HttpStatus.OK);
     }

    // Delete an Image form product
    @DeleteMapping("/{productId}/images/{imageId}")
    public ResponseEntity<String> deleteProductImage(@PathVariable("productId") Long productId,
            @PathVariable("imageId") Long imageId) throws IOException {
        productService.deleteProductImage(productId, imageId);
        return ResponseEntity.ok("Image deleted successfully.");
    }

    // add an Image in product
    @PostMapping("{productId}/images")
    public ResponseEntity<List<Image>> uploadImage(@PathVariable("productId") Long productId,
            @RequestParam("files") MultipartFile[] files) {
        try {
            List<Image> savedPhotos = productService.uploadImages(productId, files);
            return ResponseEntity.ok(savedPhotos);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null); // Handle the exception
        }
    }

    @DeleteMapping("{productId}")
    public ResponseEntity<String> DeleteProduct(@PathVariable("productId") Long productId){
        productService.deleteProduct(productId);
        return ResponseEntity.ok("Product deleted successfully.");
    }
}
