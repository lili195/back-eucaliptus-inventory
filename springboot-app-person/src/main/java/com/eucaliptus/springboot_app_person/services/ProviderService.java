package com.eucaliptus.springboot_app_person.services;


import com.eucaliptus.springboot_app_person.dtos.Message;
import com.eucaliptus.springboot_app_person.model.Provider;
import com.eucaliptus.springboot_app_person.repository.ProviderRepository;
import com.eucaliptus.springboot_app_person.utlities.ServicesUri;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@Service
public class ProviderService {

    @Autowired
    private ProviderRepository providerRepository;
    @Autowired
    private APIService apiService;
    @Autowired
    private RestTemplate restTemplate;

    public List<Provider> getAllProviders() {
        return providerRepository.findAll();
    }

    public List<Provider> getAllActiveProviders() {
        return providerRepository.findByActiveTrue();
    }

    public Optional<Provider> getProviderById(String id) {
        return providerRepository.findByIdNumber(id);
    }

    public Optional<Provider> getProviderByCompanyId(String companyId) {
        return providerRepository.findByActiveTrueAndCompany_NitCompany(companyId);
    }

    public Provider saveProvider(Provider provider) {
        return providerRepository.save(provider);
    }

    public boolean existsById(String id) {
        return providerRepository.existsByIdNumber(id);
    }

    public Optional<Provider> updateProvider(String id, Provider providerDetails) {
        return providerRepository.findByIdNumber(id).map(provider -> {
            provider.setFirstName(providerDetails.getFirstName());
            provider.setLastName(providerDetails.getLastName());
            provider.setEmail(providerDetails.getEmail());
            provider.setAddress(providerDetails.getAddress());
            provider.setPhoneNumber(providerDetails.getPhoneNumber());
            provider.setDocumentType(providerDetails.getDocumentType());
            provider.setBankName(providerDetails.getBankName());
            provider.setBankAccountNumber(providerDetails.getBankAccountNumber());
            provider.setPersonType(providerDetails.getPersonType());
            return providerRepository.save(provider);
        });
    }

    public boolean deleteProvider(String id) {
        return providerRepository.findByIdNumber(id).map(provider -> {
            provider.setActive(false);
            providerRepository.save(provider);
            return true;
        }).orElse(false);
    }

    @Transactional
    public boolean deleteProviderAndProducts(String idProvider, String token) {
        if (!deleteProvider(idProvider))
            throw new IllegalArgumentException("Error eliminando proveedor");
        if (!deleteProductsFromProvider(idProvider, token))
            throw new IllegalArgumentException("Error eliminando productos");
        return true;
    }

    private boolean deleteProductsFromProvider(String idProvider, String token){
        HttpEntity<Void> entity = new HttpEntity<>(apiService.getHeader(token));
        ResponseEntity<String> response = restTemplate.exchange(
        ServicesUri.PRODUCT_SERVICE + "/products/deleteProductsByProvider/" + idProvider,
                HttpMethod.DELETE,
                entity,
                String.class
        );
        return response.getStatusCode().is2xxSuccessful();
    }


}
