package com.eucaliptus.springboot_app_products.repository;

import com.eucaliptus.springboot_app_products.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByIdProduct(Long idProduct);

    Optional<Product> findByIdProduct(Long idProduct);

    List<Product> findByName(String name);

    List<Product> findByActiveTrue();

    boolean existsByName(String productName);

    Optional<Product> findByNameProduct(String name);
}
