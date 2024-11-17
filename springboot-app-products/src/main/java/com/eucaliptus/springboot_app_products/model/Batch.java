package com.eucaliptus.springboot_app_products.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
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
@Table(name = "batches")
public class Batch {

    @Id
    @Column(name = "id_product", nullable = false)
    private String idProduct;

    @Id
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "batch", nullable = false, columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private Date batch;

    @Column(name = "id_purchase_detail", nullable = false)
    private Long idPurchaseDetail;

    @Column(name = "quantity_available_batch", nullable = false)
    private Integer quantityAvailableBatch;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "due_date", columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private Date dueDate;

    public Batch(Integer quantityAvailableBatch, Date batch, Date dueDate) {
        this.quantityAvailableBatch = quantityAvailableBatch;
        this.batch = batch;
        this.dueDate = dueDate;
    }
}
