package com.eucaliptus.springboot_app_reports.dto;

import com.eucaliptus.springboot_app_reports.enums.EnumCategory;
import com.eucaliptus.springboot_app_reports.enums.EnumUse;
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
