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

    @ManyToOne
    @JoinColumn(name = "id_det_product", referencedColumnName = "id_det_product")
    private ProductDetail productDetail;  // Detalle del producto asociado

    @Column(name = "quantity", nullable = false)
    private int quantity;  // Nueva propiedad de cantidad total

    // Constructor con parámetros
    public Stock(Product product, Integer quantityAvailable, ProductDetail productDetail, int quantity) {
        this.product = product;
        this.quantityAvailable = quantityAvailable;
        this.productDetail = productDetail;
        this.quantity = quantity;  // Inicialización de la nueva propiedad
    }

    // Método getter para idStock
    public Long getIdStock() {
        return idStock;
    }

    // Método setter para idStock
    public void setIdStock(Long idStock) {
        this.idStock = idStock;
    }

    // Método getter para quantityAvailable
    public Integer getQuantityAvailable() {
        return quantityAvailable;
    }

    // Método setter para quantityAvailable
    public void setQuantityAvailable(Integer quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
    }

    // Método getter para la cantidad total
    public int getQuantity() {
        return quantity;
    }

    // Método setter para la cantidad total
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
