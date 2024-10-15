package com.eucaliptus.springboot_app_person.controllers;


import com.eucaliptus.springboot_app_person.model.DocumentType;
import com.eucaliptus.springboot_app_person.services.DocumentTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/document-types")
public class DocumentTypeController {

    @Autowired
    private DocumentTypeService documentTypeService;

    @GetMapping
    public List<DocumentType> getAllDocumentTypes() {
        return documentTypeService.getAllDocumentTypes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentType> getDocumentTypeById(@PathVariable Long id) {
        return documentTypeService.getDocumentTypeById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public DocumentType createDocumentType(@RequestBody DocumentType documentType) {
        return documentTypeService.saveDocumentType(documentType);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DocumentType> updateDocumentType(@PathVariable Long id, @RequestBody DocumentType documentTypeDetails) {
        return documentTypeService.updateDocumentType(id, documentTypeDetails)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocumentType(@PathVariable Long id) {
        return documentTypeService.deleteDocumentType(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
