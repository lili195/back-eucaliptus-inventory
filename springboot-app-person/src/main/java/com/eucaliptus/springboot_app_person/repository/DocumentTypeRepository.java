package com.eucaliptus.springboot_app_person.repository;

import com.eucaliptus.springboot_app_person.model.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentTypeRepository extends JpaRepository<DocumentType, Long> {
}