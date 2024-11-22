package com.eucaliptus.springboot_app_billing.repository;

import com.eucaliptus.springboot_app_billing.model.Summary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SummaryRepository extends JpaRepository<Summary, Long> {

    @Query(value = "SELECT * FROM client_summary", nativeQuery = true)
    Optional<Summary> findGlobalSummary();
}
