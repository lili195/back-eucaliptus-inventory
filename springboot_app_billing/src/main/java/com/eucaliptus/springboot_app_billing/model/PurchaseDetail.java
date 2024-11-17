package com.eucaliptus.springboot_app_billing.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "purchase_details")
public class PurchaseDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_purchase_detail")
    private int idPurchaseDetail;

    @ManyToOne
    @JoinColumn(name = "id_purchase", referencedColumnName = "id_purchase")
    private Purchase purchase;

    @Column(name = "id_product")
    private String idProduct;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "batch_purchase", nullable = false, columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private Date batchPurchase;

    @Column(name = "quantity_purchased", nullable = false)
    private Integer quantityPurchased;

    @Column(name = "purchase_price")
    private Double purchasePrice;

    @Column(name = "purchase_price_without_iva")
    private Double purchasePriceWithoutIva;

    @Column(name = "sale_price", nullable = false)
    private Double salePrice;

    @Column(name = "sale_price_without_iva", nullable = false)
    private Double salePriceWithoutIva;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "purchase_due_date", columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private Date purchaseDueDate;

}
