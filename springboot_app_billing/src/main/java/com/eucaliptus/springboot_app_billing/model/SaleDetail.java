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
@Table(name = "sale_details")
public class SaleDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sale_detail")
    private int idSaleDetail;

    @ManyToOne
    @JoinColumn(name = "id_sale", referencedColumnName = "id_sale", nullable = false)
    private Sale sale;

    @Column(name = "id_product", nullable = false)
    private String idProduct;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "batch", nullable = false, columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private Date batch;

    @Column(name = "quantitySold", nullable = false)
    private Integer quantitySold;

    @Column(name = "sale_price", nullable = false)
    private Double salePrice;

    @Column(name = "sale_price_without_iva", nullable = false)
    private Double salePriceWithoutIva;

}
