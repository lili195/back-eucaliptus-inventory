package com.eucaliptus.springboot_app_person.repository;

import com.eucaliptus.springboot_app_person.enums.EnumDocumentType;
import com.eucaliptus.springboot_app_person.model.DocumentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class DocumentTypeRepositoryTest {

    @Mock
    private DocumentTypeRepository documentTypeRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByNameType_ReturnsDocumentTypeWhenExists() {
        // Arrange
        EnumDocumentType nameType = EnumDocumentType.CC;
        DocumentType documentType = new DocumentType(nameType);
        when(documentTypeRepository.findByNameType(nameType)).thenReturn(Optional.of(documentType));

        // Act
        Optional<DocumentType> foundDocumentType = documentTypeRepository.findByNameType(nameType);

        // Assert
        assertThat(foundDocumentType).isPresent();
        assertThat(foundDocumentType.get().getNameType()).isEqualTo(nameType);
        verify(documentTypeRepository, times(1)).findByNameType(nameType);
    }

    @Test
    void testFindByNameType_ReturnsEmptyWhenNotExists() {
        // Arrange
        EnumDocumentType nameType = EnumDocumentType.CE;
        when(documentTypeRepository.findByNameType(nameType)).thenReturn(Optional.empty());

        // Act
        Optional<DocumentType> foundDocumentType = documentTypeRepository.findByNameType(nameType);

        // Assert
        assertThat(foundDocumentType).isNotPresent();
        verify(documentTypeRepository, times(1)).findByNameType(nameType);
    }

    @Test
    void testExistsByNameType_ReturnsTrueWhenExists() {
        // Arrange
        EnumDocumentType nameType = EnumDocumentType.CC;
        when(documentTypeRepository.existsByNameType(nameType)).thenReturn(true);

        // Act
        boolean exists = documentTypeRepository.existsByNameType(nameType);

        // Assert
        assertThat(exists).isTrue();
        verify(documentTypeRepository, times(1)).existsByNameType(nameType);
    }

    @Test
    void testExistsByNameType_ReturnsFalseWhenNotExists() {
        // Arrange
        EnumDocumentType nameType = EnumDocumentType.CC;
        when(documentTypeRepository.existsByNameType(nameType)).thenReturn(false);

        // Act
        boolean exists = documentTypeRepository.existsByNameType(nameType);

        // Assert
        assertThat(exists).isFalse();
        verify(documentTypeRepository, times(1)).existsByNameType(nameType);
    }

    @Test
    void testSaveDocumentType() {
        // Arrange
        EnumDocumentType nameType = EnumDocumentType.CC;
        DocumentType documentType = new DocumentType(nameType);
        when(documentTypeRepository.save(documentType)).thenReturn(documentType);

        // Act
        DocumentType savedDocumentType = documentTypeRepository.save(documentType);

        // Assert
        assertThat(savedDocumentType).isNotNull();
        assertThat(savedDocumentType.getNameType()).isEqualTo(nameType);
        verify(documentTypeRepository, times(1)).save(documentType);
    }
}
