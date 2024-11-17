package com.eucaliptus.springboot_app_billing.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "sales")

public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sale")
    private Integer idSale;

    @Column(name = "id_product", nullable = false)
    private String idProduct;  // Llave foránea de otro microservicio

    @Column(name = "quantityAvailableBatch", nullable = false)
    private Integer quantity;

    @Column(name = "unit_price", nullable = false)
    private Double unitPrice;

    @ManyToOne
    @JoinColumn(name = "id_bill", referencedColumnName = "id_bill", nullable = false)
    private Bill bill;

    public Sale(String idProduct, Integer quantity, Double unitPrice, Bill bill) {
        this.idProduct = idProduct;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.bill = bill;
    }

    public Object getIdBill() {
        return null;
    }

    public void setIdBill(Object idBill) {

    }
}
