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
@Table(name = "purchases")
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_purchase")
    private int idPurchase;

    @Column(name = "id_provider", nullable = false)
    private String idProvider;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "purchase_sale", nullable = false, columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private Date purchaseDate;

    @Column(name = "total_purchase", nullable = false)
    private Double totalPurchase;

}
