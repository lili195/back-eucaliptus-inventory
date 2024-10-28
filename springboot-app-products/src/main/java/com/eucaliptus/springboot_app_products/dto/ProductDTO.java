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
    private Long idProduct;
    private String productName;
    private String brand;
    private EnumCategory category;
    private EnumUse use;
    private Long idProvider;
    private String description;
    private Unit idUnit;
    private Integer minimumProductAmount;
    private Integer maximumProductAmount;

    public ProductDTO(String productName, String brand, EnumCategory category, EnumUse use, Long idProvider, String description, Unit idUnit, Integer minimumProductAmount, Integer maximumProductAmount) {
    }
}
