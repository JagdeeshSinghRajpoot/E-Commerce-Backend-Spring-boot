package com.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shop.entitys.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

}
