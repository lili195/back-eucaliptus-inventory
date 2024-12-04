package com.eucaliptus.springboot_app_person.services;


import com.eucaliptus.springboot_app_person.enums.EnumDocumentType;
import com.eucaliptus.springboot_app_person.model.DocumentType;
import com.eucaliptus.springboot_app_person.repository.DocumentTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio encargado de gestionar las operaciones relacionadas con los tipos de documentos.
 * Esta clase proporciona métodos para:
 * - Obtener todos los tipos de documentos.
 * - Buscar un tipo de documento por su ID.
 * - Guardar un nuevo tipo de documento.
 * - Actualizar un tipo de documento existente.
 * - Buscar un tipo de documento por su nombre.
 * - Verificar la existencia de un tipo de documento.
 * - Eliminar un tipo de documento.
 */
@Service
public class DocumentTypeService {

    @Autowired
    private DocumentTypeRepository documentTypeRepository;

    /**
     * Obtiene todos los tipos de documentos almacenados en la base de datos.
     *
     * Este método devuelve una lista con todos los tipos de documentos registrados en el repositorio.
     *
     * @return Una lista de todos los tipos de documentos.
     */
    public List<DocumentType> getAllDocumentTypes() {
        return documentTypeRepository.findAll();
    }

    /**
     * Busca un tipo de documento por su ID.
     *
     * Este método consulta la base de datos buscando un tipo de documento cuyo ID coincida con el proporcionado.
     *
     * @param id El ID del tipo de documento a buscar.
     * @return Un {@code Optional} que contiene el tipo de documento encontrado, o {@code Optional.empty()} si no se encuentra ninguno con ese ID.
     */
    public Optional<DocumentType> getDocumentTypeById(Long id) {
        return documentTypeRepository.findById(id);
    }

    /**
     * Guarda un nuevo tipo de documento en la base de datos.
     *
     * Este método guarda una nueva instancia de la clase {@code DocumentType} en el repositorio.
     *
     * @param documentType El tipo de documento que se desea guardar.
     * @return El tipo de documento guardado, incluyendo el ID asignado si es la primera vez que se guarda.
     */
    public DocumentType saveDocumentType(DocumentType documentType) {
        return documentTypeRepository.save(documentType);
    }

    /**
     * Actualiza un tipo de documento existente con los nuevos detalles proporcionados.
     *
     * Este método busca un tipo de documento con el ID proporcionado y, si se encuentra, actualiza su nombre
     * con el valor proporcionado en {@code documentTypeDetails}. Luego guarda el tipo de documento actualizado en el repositorio.
     *
     * @param id El ID del tipo de documento a actualizar.
     * @param documentTypeDetails Un objeto {@code DocumentType} que contiene los nuevos detalles del tipo de documento.
     * @return Un {@code Optional} que contiene el tipo de documento actualizado, o {@code Optional.empty()} si no se encuentra el tipo de documento.
     */
    public Optional<DocumentType> updateDocumentType(Long id, DocumentType documentTypeDetails) {
        return documentTypeRepository.findById(id).map(documentType -> {
            documentType.setNameType(documentTypeDetails.getNameType());
            return documentTypeRepository.save(documentType);
        });
    }

    /**
     * Busca un tipo de documento por su nombre.
     *
     * Este método consulta el repositorio para encontrar un tipo de documento cuyo nombre coincida con el proporcionado.
     *
     * @param name El nombre del tipo de documento a buscar.
     * @return Un {@code Optional} que contiene el tipo de documento encontrado, o {@code Optional.empty()} si no se encuentra ninguno con ese nombre.
     */
    public Optional<DocumentType> findByNameType(EnumDocumentType name){
        return documentTypeRepository.findByNameType(name);
    }

    /**
     * Verifica si existe un tipo de documento con el nombre proporcionado.
     *
     * Este método consulta el repositorio para verificar si existe un tipo de documento con el nombre dado.
     *
     * @param name El nombre del tipo de documento a verificar.
     * @return {@code true} si el tipo de documento existe, {@code false} en caso contrario.
     */
    public boolean existsByDocumentType(EnumDocumentType name) {
        return documentTypeRepository.existsByNameType(name);
    }

    /**
     * Elimina un tipo de documento de la base de datos.
     *
     * Este método busca un tipo de documento con el ID proporcionado y, si se encuentra, lo elimina del repositorio.
     *
     * @param id El ID del tipo de documento a eliminar.
     * @return {@code true} si el tipo de documento fue eliminado exitosamente, {@code false} si no se encontró el tipo de documento.
     */
    public boolean deleteDocumentType(Long id) {
        return documentTypeRepository.findById(id).map(documentType -> {
            documentTypeRepository.delete(documentType);
            return true;
        }).orElse(false);
    }
}
