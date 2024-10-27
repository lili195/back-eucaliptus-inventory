package com.eucaliptus.springboot_app_products.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_product")
    private Long idProduct;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "brand")
    private String brand;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private EnumCategory category;

    @Enumerated(EnumType.STRING)
    @Column(name = "use")
    private EnumUse use;

    @Column(name = "id_provider")
    private Long idProvider;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "id_unit", referencedColumnName = "id_unit")
    private Unit unit;

    @Column(name = "minimum_product_amount")
    private Integer minimumProductAmount;

    @Column(name = "maximum_product_amount")
    private Integer maximumProductAmount;

    public Product(String productName, String brand, String category, String use, Long idProvider, String description, Unit unit, Integer minimumProductAmount, Integer maximumProductAmount) {
        this.productName = productName;
        this.brand = brand;
        this.category = category;
        this.use = use;
        this.idProvider = idProvider;
        this.description = description;
        this.unit = unit;
        this.minimumProductAmount = minimumProductAmount;
        this.maximumProductAmount = maximumProductAmount;
    }
}
