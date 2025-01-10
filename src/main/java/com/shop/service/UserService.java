package com.shop.service;

import java.util.List;
import java.util.Optional;

import com.shop.entitys.CartItem;
import com.shop.entitys.User;

public interface UserService {
    User saveUser(User user);

    Optional<User> getUserByMobileNumber(String mobileNumber);

    List<User> getAllUsers();

    User updateUser(User user);

    void deleteUser(String mobileNumber);

    boolean userExists(String mobileNumber);

    List<CartItem> getUserCartItem(String id);

    void createCartItem(String userNumber, Long productId);

}
