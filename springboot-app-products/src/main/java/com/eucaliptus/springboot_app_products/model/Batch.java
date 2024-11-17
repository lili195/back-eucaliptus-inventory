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
@IdClass(BatchId.class)
@Table(name = "batchs")
public class Batch {

    @Id
    @Column(name = "id_product", nullable = false)
    private String idProduct;

    @Id
    @Temporal(TemporalType.DATE)
    @Column(name = "batch", nullable = false)
    private Date batch;

    @Column(name = "id_purchase_detail", nullable = false)
    private Long idPurchaseDetail;

    @Column(name = "quantity_available_batch", nullable = false)
    private Integer quantityAvailableBatch;

    @Temporal(TemporalType.DATE)
    @Column(name = "due_date")
    private Date dueDate;

    public Batch(Integer quantityAvailableBatch, Date batch, Date dueDate) {
        this.quantityAvailableBatch = quantityAvailableBatch;
        this.batch = batch;
        this.dueDate = dueDate;
    }
}
