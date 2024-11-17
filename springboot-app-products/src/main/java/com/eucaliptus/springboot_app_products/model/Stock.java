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
@Table(name = "stocks")
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_stock")
    private Long idStock;

    @ManyToOne
    @JoinColumn(name = "id_product", referencedColumnName = "id_product")
    private Product product;

    @Column(name = "id_sale_detail")
    private Long idSaleDetail;

    @Column(name = "id_purchase_detail")
    private Long idPurchaseDetail;

    @Column(name = "quantity_available", nullable = false)
    private Integer quantityAvailable;

    @Column(name = "product_sale_price", nullable = false)
    private Double productSalePrice;

    @Column(name = "product_sale_price_without_iva", nullable = false)
    private Double productSalePriceWithoutIva;

    @Column(name = "modification_date_stock", nullable = false)
    private Date modificationDateStock;

    public Stock(Product product, Integer quantityAvailable) {
        this.product = product;
        this.quantityAvailable = quantityAvailable;
    }

}