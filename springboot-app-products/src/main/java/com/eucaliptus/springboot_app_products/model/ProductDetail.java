package com.eucaliptus.springboot_app_products.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "product_details")
public class ProductDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_det_product")
    private Long idDetProduct;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "unit_price", nullable = false)
    private Double unitPrice;

    @Temporal(TemporalType.DATE)
    @Column(name = "batch", nullable = false)
    private Date batch;

    @Temporal(TemporalType.DATE)
    @Column(name = "due_date", nullable = false)
    private Date dueDate;

    public ProductDetail(Integer quantity, Double unitPrice, Date batch, Date dueDate) {
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.batch = batch;
        this.dueDate = dueDate;
    }
}
