package com.eucaliptus.springboot_app_person.repository;

import com.eucaliptus.springboot_app_person.model.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProviderRepository extends JpaRepository<Provider, Long> {
}