package com.eucaliptus.springboot_app_products.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "stocks")
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_stock")
    private Long idStock;  // ID del stock

    @ManyToOne
    @JoinColumn(name = "id_product", referencedColumnName = "id_product")
    private Product product;  // Producto asociado

    @Column(name = "quantity_available", nullable = false)
    private Integer quantityAvailable;  // Cantidad disponible en el stock

    // Constructor con parámetros
    public Stock(Product product, Integer quantityAvailable) {
        this.product = product;
        this.quantityAvailable = quantityAvailable;
    }

}