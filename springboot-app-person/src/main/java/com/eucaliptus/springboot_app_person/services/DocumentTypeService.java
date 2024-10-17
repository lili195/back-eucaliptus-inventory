package com.eucaliptus.springboot_app_person.services;


import com.eucaliptus.springboot_app_person.enums.EnumDocumentType;
import com.eucaliptus.springboot_app_person.model.DocumentType;
import com.eucaliptus.springboot_app_person.repository.DocumentTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DocumentTypeService {

    @Autowired
    private DocumentTypeRepository documentTypeRepository;

    public List<DocumentType> getAllDocumentTypes() {
        return documentTypeRepository.findAll();
    }

    public Optional<DocumentType> getDocumentTypeById(Long id) {
        return documentTypeRepository.findById(id);
    }

    public DocumentType saveDocumentType(DocumentType documentType) {
        return documentTypeRepository.save(documentType);
    }

    public Optional<DocumentType> updateDocumentType(Long id, DocumentType documentTypeDetails) {
        return documentTypeRepository.findById(id).map(documentType -> {
            documentType.setNameType(documentTypeDetails.getNameType());
            return documentTypeRepository.save(documentType);
        });
    }

    public Optional<DocumentType> findByNameType(EnumDocumentType name){
        return documentTypeRepository.findByNameType(name);
    }

    public boolean existsByDocumentType(EnumDocumentType name) {
        return documentTypeRepository.existsByNameType(name);
    }

    public boolean deleteDocumentType(Long id) {
        return documentTypeRepository.findById(id).map(documentType -> {
            documentTypeRepository.delete(documentType);
            return true;
        }).orElse(false);
    }
}
