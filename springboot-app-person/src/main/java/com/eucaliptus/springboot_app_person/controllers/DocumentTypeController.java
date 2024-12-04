package com.eucaliptus.springboot_app_person.controllers;


import com.eucaliptus.springboot_app_person.model.DocumentType;
import com.eucaliptus.springboot_app_person.services.DocumentTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para manejar las operaciones CRUD de tipos de documentos.
 *
 * Este controlador permite gestionar los tipos de documentos a través de los siguientes endpoints:
 * <ul>
 *     <li><strong>GET /api/document-types</strong>: Obtiene todos los tipos de documentos.</li>
 *     <li><strong>GET /api/document-types/{id}</strong>: Obtiene un tipo de documento por su ID.</li>
 *     <li><strong>POST /api/document-types</strong>: Crea un nuevo tipo de documento.</li>
 *     <li><strong>PUT /api/document-types/{id}</strong>: Actualiza un tipo de documento existente por su ID.</li>
 *     <li><strong>DELETE /api/document-types/{id}</strong>: Elimina un tipo de documento por su ID.</li>
 * </ul>
 *
 */
@RestController
@RequestMapping("/api/document-types")
public class DocumentTypeController {

    @Autowired
    private DocumentTypeService documentTypeService;

    /**
     * Obtiene todos los tipos de documentos.
     *
     * @return una lista de todos los tipos de documentos.
     */
    @GetMapping
    public List<DocumentType> getAllDocumentTypes() {
        return documentTypeService.getAllDocumentTypes();
    }

    /**
     * Obtiene un tipo de documento por su ID.
     *
     * @param id el ID del tipo de documento.
     * @return una respuesta con el tipo de documento encontrado, o un estado "Not Found" si no se encuentra.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DocumentType> getDocumentTypeById(@PathVariable Long id) {
        return documentTypeService.getDocumentTypeById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Crea un nuevo tipo de documento.
     *
     * @param documentType el tipo de documento a crear.
     * @return el tipo de documento creado.
     */
    @PostMapping
    public DocumentType createDocumentType(@RequestBody DocumentType documentType) {
        return documentTypeService.saveDocumentType(documentType);
    }

    /**
     * Actualiza un tipo de documento existente por su ID.
     *
     * @param id                 el ID del tipo de documento a actualizar.
     * @param documentTypeDetails los detalles del tipo de documento a actualizar.
     * @return una respuesta con el tipo de documento actualizado, o un estado "Not Found" si no se encuentra.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DocumentType> updateDocumentType(@PathVariable Long id, @RequestBody DocumentType documentTypeDetails) {
        return documentTypeService.updateDocumentType(id, documentTypeDetails)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Elimina un tipo de documento por su ID.
     *
     * @param id el ID del tipo de documento a eliminar.
     * @return una respuesta "No Content" si el tipo de documento fue eliminado, o "Not Found" si no se encuentra.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocumentType(@PathVariable Long id) {
        return documentTypeService.deleteDocumentType(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
