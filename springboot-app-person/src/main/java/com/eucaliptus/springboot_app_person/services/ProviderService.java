package com.eucaliptus.springboot_app_person.services;


import com.eucaliptus.springboot_app_person.model.Provider;
import com.eucaliptus.springboot_app_person.repository.ProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProviderService {

    @Autowired
    private ProviderRepository providerRepository;

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
        return providerRepository.findByActiveTrueAndCompany_IdNumber(companyId);
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
            provider.setCompany(providerDetails.getCompany());
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


}
