package com.shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shop.entitys.Image;

@Repository
// @EnableJpaRepositories
public interface ImageRepository extends JpaRepository<Image, Long> {
    // @Query("SELECT c.product FROM image c WHERE c.id = :productId")
    List<Image> findByProductId(@Param("productId") Long productId);

}
