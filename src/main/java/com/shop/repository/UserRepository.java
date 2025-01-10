package com.shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shop.entitys.CartItem;
import com.shop.entitys.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    @Query("SELECT c.cartitems FROM User c WHERE c.id = :UserId")
    List<CartItem> findCartItemsByUserId(@Param("UserId") String UserId);

}
