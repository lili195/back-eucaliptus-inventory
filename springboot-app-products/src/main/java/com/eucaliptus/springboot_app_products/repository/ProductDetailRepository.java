package com.eucaliptus.springboot_app_products.repository;

import com.eucaliptus.springboot_app_products.model.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetail, Long> {

    boolean existsByIdDetProduct(Long idDetProduct);

    Optional<ProductDetail> findByIdDetProduct(Long idDetProduct);
}
