package com.eucaliptus.springboot_app_person.repository;

import com.eucaliptus.springboot_app_person.model.Provider;
import com.eucaliptus.springboot_app_person.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProviderRepository extends JpaRepository<Provider, Long> {

    boolean existsByIdProvider(Long id);

    List<Provider> findByActiveTrue();

    Optional<Provider> findByPerson_IdNumber(String personId);

}