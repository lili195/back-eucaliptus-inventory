package com.eucaliptus.springboot_app_billing.mappers;

import com.eucaliptus.springboot_app_billing.dto.BillDTO;
import com.eucaliptus.springboot_app_billing.dto.SaleDTO;
import com.eucaliptus.springboot_app_billing.model.Bill;

import java.util.List;
import java.util.stream.Collectors;

public class BillMapper {
    public static Bill billDTOToBill(BillDTO billDTO) {
        Bill bill = new Bill();
        bill.setIdBill(billDTO.getIdBill());
        bill.setBillDate(billDTO.getBillDate());
        bill.setTotal(billDTO.getTotal());
        bill.setIdPerson(billDTO.getIdPerson());
        return bill;
    }

    public static BillDTO billToBillDTO(Bill bill) {
        BillDTO billDTO = new BillDTO();
        billDTO.setIdBill(bill.getIdBill());
        billDTO.setBillDate(bill.getBillDate());
        billDTO.setTotal(bill.getTotal());
        billDTO.setIdPerson(bill.getIdPerson());
        billDTO.setIdClient(bill.getClient().getIdClient()); // Asumiendo que Client no es nulo
        List<SaleDTO> saleDTOs = bill.getSales().stream()
                .map(SaleMapper::saleToSaleDTO)
                .collect(Collectors.toList());
        billDTO.setSales(saleDTOs);
        return billDTO;
    }
}
