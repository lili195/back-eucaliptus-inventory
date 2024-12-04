package com.eucaliptus.springboot_app_person.controllers;

import com.eucaliptus.springboot_app_person.dtos.Message;
import com.eucaliptus.springboot_app_person.dtos.PersonDTO;
import com.eucaliptus.springboot_app_person.dtos.ProviderDTO;
import com.eucaliptus.springboot_app_person.model.Person;
import com.eucaliptus.springboot_app_person.model.Provider;
import com.eucaliptus.springboot_app_person.services.CompanyService;
import com.eucaliptus.springboot_app_person.services.PersonService;
import com.eucaliptus.springboot_app_person.services.ProviderService;
import com.eucaliptus.springboot_app_person.services.APIService;
import com.eucaliptus.springboot_app_person.services.DocumentTypeService;
import com.eucaliptus.springboot_app_person.enums.EnumRole;
import com.eucaliptus.springboot_app_person.mappers.ProviderMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProviderControllerTest {

    @Mock
    private ProviderService providerService;
    @Mock
    private PersonService personService;
    @Mock
    private CompanyService companyService;
    @Mock
    private APIService apiService;
    @Mock
    private DocumentTypeService documentTypeService;

    @InjectMocks
    private ProviderController providerController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(providerController).build();
    }

    @Test
    void getAllProviders_shouldReturnOk_whenProvidersExist() throws Exception {
        // Arrange
        when(providerService.getAllActiveProviders()).thenReturn(List.of(new Provider()));

        // Act & Assert
        mockMvc.perform(get("/person/providers/all"))
                .andExpect(status().isOk());
    }

    @Test
    void getProviderById_shouldReturnOk_whenProviderExists() throws Exception {
        // Arrange
        String id = "1";
        Provider provider = new Provider();
        when(providerService.getProviderById(id)).thenReturn(Optional.of(provider));

        // Act & Assert
        mockMvc.perform(get("/person/providers/getProviderById/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    void getProviderById_shouldReturnBadRequest_whenProviderNotFound() throws Exception {
        // Arrange
        String id = "1";
        when(personService.getActivePersonById(id)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/person/providers/getProviderById/{id}", id))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"message\":\"Proveedor no encontrado\"}"));
    }

    @Test
    void validateNewProvider_shouldReturnBadRequest_whenPersonExists() throws Exception {
        // Arrange
        ProviderDTO providerDTO = new ProviderDTO();
        providerDTO.setPersonDTO(new PersonDTO());
        when(personService.existsByIdPerson(providerDTO.getPersonDTO().getIdPerson())).thenReturn(true);

        // Act & Assert
        mockMvc.perform(post("/person/providers/validateNewProvider")
                        .contentType("application/json")
                        .content("{\"personDTO\":{\"idPerson\":\"existingPersonId\",\"personType\":\"NATURAL\"}}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"message\":\"Persona ya existente\"}"));
    }

    @Test
    void createProvider_shouldReturnOk_whenProviderIsCreated() throws Exception {
        // Arrange
        ProviderDTO providerDTO = new ProviderDTO();
        when(personService.existsByIdPerson(providerDTO.getPersonDTO().getIdPerson())).thenReturn(false);
        when(providerService.saveProvider(any(Provider.class))).thenReturn(new Provider());

        // Act & Assert
        mockMvc.perform(post("/person/providers/addProvider")
                        .contentType("application/json")
                        .content("{\"personDTO\":{\"idPerson\":\"newPersonId\",\"personType\":\"JURIDICA\"}}"))
                .andExpect(status().isOk());
    }

    @Test
    void updateProvider_shouldReturnBadRequest_whenProviderDoesNotExist() throws Exception {
        // Arrange
        String id = "1";
        ProviderDTO providerDTO = new ProviderDTO();
        when(providerService.existsById(id)).thenReturn(false);

        // Act & Assert
        mockMvc.perform(put("/person/providers/updateProvider/{id}", id)
                        .contentType("application/json")
                        .content("{\"personDTO\":{\"idPerson\":\"newPersonId\",\"personType\":\"JURIDICA\"}}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"message\":\"Este proveedor no existe\"}"));
    }

    @Test
    void deleteProvider_shouldReturnOk_whenProviderIsDeleted() throws Exception {
        // Arrange
        String id = "1";
        when(providerService.existsById(id)).thenReturn(true);
        when(providerService.deleteProviderAndProducts(id, "token")).thenReturn(true);

        // Act & Assert
        mockMvc.perform(delete("/person/providers/deleteProvider/{idProvider}", id))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"message\":\"Proveedor eliminado\"}"));
    }

    @Test
    void deleteProvider_shouldReturnInternalServerError_whenDbErrorOccurs() throws Exception {
        // Arrange
        String id = "1";
        when(providerService.existsById(id)).thenReturn(true);
        when(providerService.deleteProviderAndProducts(id, "token")).thenReturn(false);

        // Act & Assert
        mockMvc.perform(delete("/person/providers/deleteProvider/{idProvider}", id))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json("{\"message\":\"Error con la bd\"}"));
    }
}
