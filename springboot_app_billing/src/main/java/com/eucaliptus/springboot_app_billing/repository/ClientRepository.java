package com.eucaliptus.springboot_app_billing.repository;

import com.eucaliptus.springboot_app_billing.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, String> {

    Optional<Client> findByEmailClient(String email);

    List<Client> findByNameClient(String nameClient);

    boolean existsByEmailClient(String email);
}