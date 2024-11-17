package com.eucaliptus.springboot_app_person.repository;

import com.eucaliptus.springboot_app_person.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, String> {

    boolean existsByNitCompany(String nitCompany);

}
