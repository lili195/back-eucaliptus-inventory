package com.eucaliptus.springboot_app_billing.service;

import com.eucaliptus.springboot_app_billing.dto.PurchaseDTO;
import com.eucaliptus.springboot_app_billing.dto.PurchaseDetailDTO;
import com.eucaliptus.springboot_app_billing.mappers.PurchaseDetailMapper;
import com.eucaliptus.springboot_app_billing.mappers.PurchaseMapper;
import com.eucaliptus.springboot_app_billing.model.Purchase;
import com.eucaliptus.springboot_app_billing.model.PurchaseDetail;
import com.eucaliptus.springboot_app_billing.repository.PurchaseRepository;
import com.eucaliptus.springboot_app_billing.utlities.ServicesUri;
import com.eucaliptus.springboot_app_person.dtos.ProviderDTO;
import com.eucaliptus.springboot_app_products.dto.NewBatchDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PurchaseService {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private PurchaseRepository purchaseRepository;
    @Autowired
    private PurchaseDetailService purchaseDetailService;

    public Optional<Purchase> getById(int id) {
        return purchaseRepository.findById(id);
    }

    public List<Purchase> getAll() {
        return purchaseRepository.findAll();
    }

    public Purchase savePurchase(Purchase purchase) {
        return purchaseRepository.save(purchase);
    }

    @Transactional
    public Purchase saveNewPurchase(PurchaseDTO purchaseDTO, HttpServletRequest request) {
        if (!existsProviderId(purchaseDTO.getProviderId(), getTokenByRequest(request)))
            throw new IllegalArgumentException("Este proveedor no existe");
        purchaseDTO.setTotalPurchase(calculateTotal(purchaseDTO.getPurchaseDetails()));
        Purchase purchaseSaved = savePurchase(PurchaseMapper.purchaseDTOToPurchase(purchaseDTO));
        ArrayList<PurchaseDetail> purchases = new ArrayList<>();
        for (PurchaseDetailDTO purchaseDetailDTO: purchaseDTO.getPurchaseDetails()){
            PurchaseDetail purchaseDetail = PurchaseDetailMapper.purchaseDetailDTOToPurchaseDetail(purchaseDetailDTO);
            purchaseDetail.setPurchase(purchaseSaved);
            purchases.add(purchaseDetailService.savePurchaseDetail(purchaseDetail));
        }
        if (!createBatches(purchases, getTokenByRequest(request)))
            throw new IllegalArgumentException("Error al crear los lotes");
        return purchaseSaved;
    }

    public boolean existsProviderId(String  providerId, String token) {
        try{
            HttpEntity<String> entity = new HttpEntity<>(getHeader(token));
            ResponseEntity<ProviderDTO> response = restTemplate.exchange(
                    ServicesUri.PERSON_SERVICE + "/person/providers/getProviderById/" + providerId,
                    HttpMethod.GET,
                    entity,
                    ProviderDTO.class
            );
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean createBatches(List<PurchaseDetail> purchases, String token) {
        List<NewBatchDTO> batches = purchases.stream().map(PurchaseDetailMapper::purchaseDetailToNewBatchDTO).toList();
        try{
            HttpEntity<List<NewBatchDTO>> entity = new HttpEntity<>(batches, getHeader(token));
            ResponseEntity<Object> response = restTemplate.exchange(
                    ServicesUri.PRODUCT_SERVICE + "/products/stock/addBatches",
                    HttpMethod.POST,
                    entity,
                    Object.class
            );
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    private Double calculateTotal(List<PurchaseDetailDTO> purchases) {
        double total = 0;
        for (PurchaseDetailDTO purchaseDetail: purchases)
            total += purchaseDetail.getQuantityPurchased()*purchaseDetail.getPurchasePrice();
        return total;
    }


    public String getTokenByRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        if (authHeader != null && authHeader.startsWith("Bearer "))
            token = authHeader.substring(7);
        return token;
    }

    private HttpHeaders getHeader(String token){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        return headers;
    }
}
