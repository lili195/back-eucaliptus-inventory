package com.eucaliptus.springboot_app_billing.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Data
@Getter
@Setter
@AllArgsConstructor
public class DatesDTO {
    Date startDate;
    Date endDate;
}
