package com.eucaliptus.springboot_app_reports.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ReportDTO {
    private ProductDTO product;
    private Long quantity;
    private Long totalPrice;
}
