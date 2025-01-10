package com.shop.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop.entitys.CartItem;
import com.shop.entitys.Product;
import com.shop.entitys.User;
import com.shop.exception.ResourceNotFoundException;
import com.shop.repository.CartItemRepository;
import com.shop.repository.ProductRepository;
import com.shop.repository.UserRepository;
import com.shop.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> getUserByMobileNumber(String mobileNumber) {
        return userRepository.findById(mobileNumber);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User updateUser(User user) {
        if (!userRepository.existsById(user.getMobileNumber())) {
            throw new ResourceNotFoundException("User not found with mobile number: " + user.getMobileNumber());
        }
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(String mobileNumber) {
        if (!userRepository.existsById(mobileNumber)) {
            throw new ResourceNotFoundException("User not found with mobile number: " + mobileNumber);
        }
        userRepository.deleteById(mobileNumber);
    }

    @Override
    public boolean userExists(String mobileNumber) {
        return userRepository.existsById(mobileNumber);
    }

    public List<CartItem> getUserCartItem(String id) {
        return userRepository.findCartItemsByUserId(id);

    }

    public void createCartItem(String userNumber, Long productId) {
        System.out.println("in create Cart");
        User user = this.userRepository.findById(userNumber)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id - " + userNumber));

        Product product = this.productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product is not found with id - " + productId));

        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(2);
        cartItem.setUser(user);
        cartItemRepository.save(cartItem);

        return;
    }

}
