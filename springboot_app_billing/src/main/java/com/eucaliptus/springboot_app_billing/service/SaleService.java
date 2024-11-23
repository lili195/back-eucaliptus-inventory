package com.eucaliptus.springboot_app_billing.service;

import com.eucaliptus.springboot_app_billing.dto.PurchaseDetailDTO;
import com.eucaliptus.springboot_app_billing.dto.SaleDTO;
import com.eucaliptus.springboot_app_billing.dto.SaleDetailDTO;
import com.eucaliptus.springboot_app_billing.mappers.ClientMapper;
import com.eucaliptus.springboot_app_billing.mappers.SaleDetailMapper;
import com.eucaliptus.springboot_app_billing.mappers.SaleMapper;
import com.eucaliptus.springboot_app_billing.model.Client;
import com.eucaliptus.springboot_app_billing.model.Sale;
import com.eucaliptus.springboot_app_billing.model.SaleDetail;
import com.eucaliptus.springboot_app_billing.repository.SaleRepository;
import com.eucaliptus.springboot_app_billing.utlities.ServicesUri;
import com.eucaliptus.springboot_app_person.dtos.SellerDTO;
import com.eucaliptus.springboot_app_products.dto.RequestBatchDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SaleService {

    @Autowired
    private SaleRepository saleRepository;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ClientService clientService;
    @Autowired
    private SaleDetailService saleDetailService;
    @Autowired
    private APIService apiService;

    public List<Sale> getSalesByDate(Date date) {
        return saleRepository.findBySaleDate(date);
    }

    public Optional<Sale> getSaleById(Integer id) {
        return saleRepository.findById(id);
    }

    public Sale saveBill(Sale sale) {
        return saleRepository.save(sale);
    }

    @Transactional
    public Sale addSale(SaleDTO saleDTO, HttpServletRequest request){
        String sellerId = getIdSeller(apiService.getTokenByRequest(request));
        if (sellerId.isEmpty())
            throw new IllegalArgumentException("Vendedor no encontrado");
        saleDTO.setIdSeller(sellerId);
        List<SaleDetailDTO> saleDetailDTOS = getSaleDetails(saleDTO, apiService.getTokenByRequest(request));
        saleDTO.setTotal(this.calculateTotal(saleDetailDTOS));
        Client clientSaved = clientService.saveClient(ClientMapper.clientDTOToClient(saleDTO.getClientDTO()));
        Sale sale = SaleMapper.saleDTOToSale(saleDTO);
        sale.setClient(clientSaved);
        sale.setIdSeller(sellerId);
        sale = saleRepository.save(sale);
        List<SaleDetail> saleDetailSaved = new ArrayList<>();
        for (SaleDetailDTO saleDetailDTO : saleDetailDTOS) {
            SaleDetail saleDetail = SaleDetailMapper.saleDetailDTOToSaleDetail(saleDetailDTO);
            saleDetail.setSale(sale);
            saleDetailSaved.add(saleDetailService.saveSaleDetail(saleDetail));
        }
        if (!updateStock(saleDetailSaved, apiService.getTokenByRequest(request)))
            throw new IllegalArgumentException("Error actualizando lotes");
        return sale;
    }

    private String getIdSeller(String token){
        try{
            HttpEntity<String> entity = new HttpEntity<>(apiService.getHeader(token));
            ResponseEntity<SellerDTO> response = restTemplate.exchange(
                    ServicesUri.PERSON_SERVICE + "/person/sellers/getSellerInfoByToken",
                    HttpMethod.GET,
                    entity,
                    SellerDTO.class
            );
            return (response.getStatusCode().is2xxSuccessful())? response.getBody().getPersonDTO().getIdPerson(): "";
        } catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }

    private List<SaleDetailDTO> getSaleDetails(SaleDTO saleDTO, String token){
        try {
            List<RequestBatchDTO> requestBatch = saleDTO.getSaleDetails().stream().map(SaleDetailMapper::saleDetailDTOToRequestBatchDTO).toList();
            HttpEntity<List<RequestBatchDTO>> entity = new HttpEntity<>(requestBatch, apiService.getHeader(token));
            ResponseEntity<List<SaleDetailDTO>> response = restTemplate.exchange(
                    ServicesUri.PRODUCT_SERVICE + "/products/stock/requestBatches",
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<List<SaleDetailDTO>>() {}
            );
            return (response.getStatusCode().is2xxSuccessful())? response.getBody() : new ArrayList<>();
        } catch (Exception e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private boolean updateStock(List<SaleDetail> saleDetails, String token){
        try{
            List<SaleDetailDTO> saleDetailDTOS = saleDetails.stream().map(SaleDetailMapper::saleDetailToSaleDetailDTO).toList();
            HttpEntity<List<SaleDetailDTO>> entity = new HttpEntity<>(saleDetailDTOS, apiService.getHeader(token));
            ResponseEntity<String> response = restTemplate.exchange(
                    ServicesUri.PRODUCT_SERVICE + "/products/stock/reduceStock",
                    HttpMethod.POST,
                    entity,
                   String.class
            );
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    private Double calculateTotal(List<SaleDetailDTO> sales) {
        double total = 0;
        for (SaleDetailDTO saleDetailDTO: sales)
            total += saleDetailDTO.getQuantitySold()*saleDetailDTO.getSalePrice();
        return total;
    }

}
