package com.eucaliptus.springboot_app_person.repository;

import com.eucaliptus.springboot_app_person.model.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProviderRepository extends JpaRepository<Provider, String> {

    boolean existsByIdNumber(String id);

    List<Provider> findByActiveTrue();

    Optional<Provider> findByIdNumber(String personId);

    Optional<Provider> findByActiveTrueAndCompany_NitCompany(String companyId);

}