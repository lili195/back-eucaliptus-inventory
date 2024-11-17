package com.eucaliptus.springboot_app_person.services;

import com.eucaliptus.springboot_app_person.model.Company;
import com.eucaliptus.springboot_app_person.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    public List<Company> findAll() {
        return companyRepository.findAll();
    }

    public Optional<Company> findByNit(String id) {
        return companyRepository.findById(id);
    }

    public boolean existsByNItCompany(String id) {
        return companyRepository.existsByNitCompany(id);
    }

    public Company save(Company company) {
        return companyRepository.save(company);
    }

    public Optional<Company> update(String nitCompany, Company companyDetails) {
        return companyRepository.findById(nitCompany).map(company -> {
            company.setNameCompany(companyDetails.getNameCompany());
            company.setEmailCompany(companyDetails.getEmailCompany());
            company.setPhoneNumberCompany(companyDetails.getPhoneNumberCompany());
            company.setAddressCompany(companyDetails.getAddressCompany());
            return companyRepository.save(company);
        });
    }
}
