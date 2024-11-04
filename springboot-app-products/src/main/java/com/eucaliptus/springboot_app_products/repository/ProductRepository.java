package com.eucaliptus.springboot_app_products.repository;

import com.eucaliptus.springboot_app_products.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    boolean existsByIdProduct(String idProduct);

    Optional<Product> findByIdProduct(String idProduct);

    List<Product> findByActiveTrue();

    List<Product> findByIdProviderAndActiveTrue(Long idProvider);

    boolean existsByName(String productName);

    Optional<Product> findByName(String name);
}
