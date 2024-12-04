package com.eucaliptus.springboot_app_person.repository;

import com.eucaliptus.springboot_app_person.enums.EnumDocumentType;
import com.eucaliptus.springboot_app_person.model.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para acceder y manejar entidades de tipo {@link DocumentType} en la base de datos.
 *
 * La interfaz {@link DocumentTypeRepository} extiende {@link JpaRepository}, lo que permite realizar
 * operaciones CRUD (crear, leer, actualizar y eliminar) sobre las entidades {@link DocumentType}.
 * Además, incluye métodos específicos para buscar y verificar tipos de documento en la base de datos
 * basándose en su nombre (de tipo {@link EnumDocumentType}).
 */

@Repository
public interface DocumentTypeRepository extends JpaRepository<DocumentType, Long> {

    /**
     * Encuentra un tipo de documento por su nombre (de tipo {@link EnumDocumentType}).
     *
     * @param name El nombre del tipo de documento que se desea buscar.
     * @return Un {@link Optional} que contiene el {@link DocumentType} encontrado,
     *         o {@link Optional#empty()} si no se encuentra ningún tipo de documento con el nombre proporcionado.
     */
    Optional<DocumentType> findByNameType(EnumDocumentType name);

    /**
     * Verifica si existe un tipo de documento con el nombre especificado (de tipo {@link EnumDocumentType}).
     *
     * @param name El nombre del tipo de documento que se desea verificar.
     * @return {@code true} si existe un tipo de documento con el nombre proporcionado;
     *         {@code false} si no existe.
     */
    boolean existsByNameType(EnumDocumentType name);
}