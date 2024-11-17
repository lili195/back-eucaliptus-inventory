package com.eucaliptus.springboot_app_products.model;

import com.eucaliptus.springboot_app_products.enums.EnumCategory;
import com.eucaliptus.springboot_app_products.enums.EnumUse;
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
    @Column(name = "id_product")
    private String idProduct;

    @ManyToOne
    @JoinColumn(name = "id_unit", referencedColumnName = "id_unit")
    private Unit unit;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "brand", nullable = false)
    private String brand;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private EnumCategory category;

    @Enumerated(EnumType.STRING)
    @Column(name = "use", nullable = false)
    private EnumUse use;

    @Column(name = "description")
    private String description;

    @Column(name = "id_provider")
    private String idProvider;

    @Column(name = "minimum_product_amount")
    private Integer minimumProductAmount;

    @Column(name = "maximum_product_amount")
    private Integer maximumProductAmount;

    @Column(name = "isActive")
    private boolean active;

    public Product(String idProduct, String productName, String brand, String category, String use, String idProvider, String description, Unit unit, Integer minimumProductAmount, Integer maximumProductAmount) {
        this.idProduct = idProduct;
        this.productName = productName;
        this.brand = brand;
        this.category = EnumCategory.valueOf(category);
        this.use = EnumUse.valueOf(use);
        this.idProvider = idProvider;
        this.description = description;
        this.unit = unit;
        this.minimumProductAmount = minimumProductAmount;
        this.maximumProductAmount = maximumProductAmount;
        this.active = true;
    }

}
