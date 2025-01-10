package com.shop.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.shop.entitys.CartItem;
import com.shop.entitys.User;
import com.shop.exception.ResourceNotFoundException;
import com.shop.service.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@Validated
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // create User
    @PostMapping
    public ResponseEntity<String> createUser(@Validated @RequestBody User user) {
        if (userService.userExists(user.getMobileNumber())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("User with mobile number " + user.getMobileNumber() + " already exists.");
        }
        User createdUser = userService.saveUser(user);
        return new ResponseEntity<>("User created successfully with mobile number " + createdUser.getMobileNumber(),
                HttpStatus.CREATED);
    }

    // find user using mobile number
    @GetMapping("/{mobileNumber}")
    public ResponseEntity<User> getUserByMobileNumber(@PathVariable("mobileNumber") String mobileNumber) {
        Optional<User> user = userService.getUserByMobileNumber(mobileNumber);
        return user.map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with mobile number: " + mobileNumber));
    }

    // get All user
    @GetMapping("/")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);

    }

    // update user
    @PutMapping("/{mobileNumber}")
    public ResponseEntity<User> updateUser(@PathVariable("mobileNumber") String mobileNumber,
            @Validated @RequestBody User user) {
        user.setMobileNumber(mobileNumber);
        try {
            User updatedUser = userService.updateUser(user);
            return ResponseEntity.ok(updatedUser);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("User not found with mobile number: " + mobileNumber);
        }
    }

    // delete user
    @DeleteMapping("/{mobileNumber}")
    public ResponseEntity<Void> deleteUser(@PathVariable("mobileNumber") String mobileNumber) {
        try {
            userService.deleteUser(mobileNumber);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("User not found with mobile number: " + mobileNumber);
        }
    }

    // Get A Cart
    @GetMapping("cart/{mobileNumber}")
    public ResponseEntity<List<CartItem>> getUserCartItem(@PathVariable("mobileNumber") String mobileNumber) {
        List<CartItem> cartItems = userService.getUserCartItem(mobileNumber);
        return ResponseEntity.ok(cartItems);
    }

    // Create CartItem
    @PostMapping("/{userNumber}/product/{productId}")
    public String setCartItem(@PathVariable("userNumber") String userNumber,
            @PathVariable("productId") Long productId) {
        System.out.println("in create Cart");
        userService.createCartItem(userNumber, productId);
        return "save";
    }

}
