package com.eucaliptus.springboot_app_products.repository;

import com.eucaliptus.springboot_app_products.dto.ProductExpiringSoonDTO;
import com.eucaliptus.springboot_app_products.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    @Autowired
    JdbcTemplate jdbcTemplate = new JdbcTemplate();

    boolean existsByIdProduct(String idProduct);

    Optional<Product> findByIdProduct(String idProduct);

    List<Product> findByActiveTrue();

    List<Product> findByIdProviderAndActiveTrue(String idProvider);

    boolean existsByProductName(String productName);

    Optional<Product> findByProductName(String name);


    public default List<ProductExpiringSoonDTO> findProductsExpiringSoon() {
        String sql = "SELECT * FROM productos_a_vencer_en_7_dias()";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            ProductExpiringSoonDTO dto = new ProductExpiringSoonDTO();
            dto.setIdProduct(rs.getString("id_product"));
            dto.setProductName(rs.getString("product_name"));
            dto.setQuantity(rs.getInt("quantityAvailableBatch"));
            dto.setDueDate(rs.getDate("due_date"));
            return dto;
        });
    }
}
