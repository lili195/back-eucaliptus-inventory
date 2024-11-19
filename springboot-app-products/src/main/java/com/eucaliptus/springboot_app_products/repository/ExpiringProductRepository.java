package com.eucaliptus.springboot_app_products.repository;

import com.eucaliptus.springboot_app_products.model.ExpiringProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ExpiringProductRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<ExpiringProduct> findProductsExpiringInSevenDays() {
        String sql = "SELECT * FROM products_that_expire_in_seven_days()";

        return jdbcTemplate.query(sql, (ResultSet rs, int rowNum) -> mapRowToExpiringProduct(rs));
    }

    private ExpiringProduct mapRowToExpiringProduct(ResultSet rs) throws SQLException {
        ExpiringProduct product = new ExpiringProduct();
        product.setIdProduct(rs.getString("id_product"));
        product.setProductName(rs.getString("product_name"));
        product.setQuantity(rs.getInt("quantity"));
        product.setDueDate(rs.getDate("due_date").toLocalDate());
        return product;
    }
}
