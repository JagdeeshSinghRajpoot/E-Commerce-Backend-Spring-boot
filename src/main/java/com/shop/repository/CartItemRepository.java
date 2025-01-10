package com.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shop.entitys.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

}
