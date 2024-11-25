package com.eucaliptus.springboot_app_billing.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class UnitDTO {
    private int idUnit;
    private String unitName;
    private String description;
}
