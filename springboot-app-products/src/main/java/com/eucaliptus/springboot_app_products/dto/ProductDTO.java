package com.eucaliptus.springboot_app_products.dto;

import com.eucaliptus.springboot_app_products.enums.EnumCategory;
import com.eucaliptus.springboot_app_products.enums.EnumUse;
import com.eucaliptus.springboot_app_products.model.Unit;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class ProductDTO {
    private String idProduct;
    private String productName;
    private String brand;
    private EnumCategory category;
    private EnumUse use;
    private String idProvider;
    private String description;
    private UnitDTO unitDTO;
    private Integer minimumProductAmount;
    private Integer maximumProductAmount;
    private boolean active;
}
