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

    public Optional<Provider> getProviderById(Long id) {
        return providerRepository.findById(id);
    }

    public Provider saveProvider(Provider provider) {
        return providerRepository.save(provider);
    }

    public boolean existsById(Long id) {
        return providerRepository.existsByIdProvider(id);
    }

    public Optional<Provider> updateProvider(Long id, Provider providerDetails) {
        return providerRepository.findById(id).map(provider -> {
            provider.setPersonType(providerDetails.getPersonType());
            provider.setCompany(providerDetails.getCompany());
            return providerRepository.save(provider);
        });
    }

    public boolean deleteProvider(Long id) {
        return providerRepository.findById(id).map(provider -> {
            providerRepository.delete(provider);
            return true;
        }).orElse(false);
    }
}
