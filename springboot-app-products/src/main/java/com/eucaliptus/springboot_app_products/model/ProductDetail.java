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

    @Column(name = "input_unit_price", nullable = false)
    private Double inputUnitPrice;

    @Column(name = "input_unit_price_without_iva", nullable = false)
    private Double inputUnitPriceWithoutIVA;

    @Column(name = "output_unit_price", nullable = false)
    private Double outputUnitPrice;

    @Column(name = "output_unit_price_without_iva", nullable = false)
    private Double outputUnitPriceWithoutIVA;

    @Temporal(TemporalType.DATE)
    @Column(name = "batch", nullable = false)
    private Date batch;

    @Temporal(TemporalType.DATE)
    @Column(name = "due_date")
    private Date dueDate;

    @ManyToOne
    @JoinColumn(name = "id_stock", referencedColumnName = "id_stock")
    private Stock stock;

    public ProductDetail(Integer quantity, Double inputUnitPrice, Double inputUnitPriceWithoutIVA, Double outputUnitPrice, Double outputUnitPriceWithoutIVA, Date batch, Date dueDate, Stock stock) {
        this.quantity = quantity;
        this.inputUnitPrice = inputUnitPrice;
        this.inputUnitPriceWithoutIVA = inputUnitPriceWithoutIVA;
        this.outputUnitPrice = outputUnitPrice;
        this.outputUnitPriceWithoutIVA = outputUnitPriceWithoutIVA;
        this.batch = batch;
        this.dueDate = dueDate;
        this.stock = stock;
    }
}
