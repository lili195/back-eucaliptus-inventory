package com.eucaliptus.springboot_app_products.dto;

import com.eucaliptus.springboot_app_products.enums.EnumCategory;
import com.eucaliptus.springboot_app_products.enums.EnumUse;
import com.eucaliptus.springboot_app_products.model.Unit;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class UpdateProductDTO {
    private String productName;
    private String oldProductName;
    private String description;
    private String oldDescription;
    private Unit idUnit;
    private int oldIdUnit;
    private Integer minimumProductAmount;
    private Integer oldMinimumProductAmount;
    private Integer maximumProductAmount;
    private Integer oldMaximumProductAmount;

}
