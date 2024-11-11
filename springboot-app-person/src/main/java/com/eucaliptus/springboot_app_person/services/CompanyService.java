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

    public Optional<Company> findById(String id) {
        return companyRepository.findById(id);
    }

    public boolean existsByNItCompany(String id) {
        return companyRepository.existsByIdNumber(id);
    }

    public boolean deleteCompany(String nitCompany) {
        return companyRepository.findById(nitCompany).map(company -> {
            company.setActive(false);
            companyRepository.save(company);
            return true;
        }).orElse(false);
    }

    public Company save(Company company) {
        return companyRepository.save(company);
    }

    public Optional<Company> update(String nitCompany, Company companyDetails) {
        return companyRepository.findById(nitCompany).map(company -> {
            company.setFirstName(companyDetails.getFirstName());
            company.setPhoneNumber(companyDetails.getPhoneNumber());
            company.setEmail(companyDetails.getEmail());
            company.setAddress(companyDetails.getAddress());
            return companyRepository.save(company);
        });
    }


}
