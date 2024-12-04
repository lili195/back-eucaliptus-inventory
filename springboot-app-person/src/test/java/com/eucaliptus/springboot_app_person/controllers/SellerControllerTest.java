package com.eucaliptus.springboot_app_person.controllers;

import com.eucaliptus.springboot_app_person.dtos.Message;
import com.eucaliptus.springboot_app_person.dtos.SellerDTO;
import com.eucaliptus.springboot_app_person.mappers.SellerMapper;
import com.eucaliptus.springboot_app_person.model.Seller;
import com.eucaliptus.springboot_app_person.services.*;
import com.eucaliptus.springboot_app_person.security.JwtTokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class SellerControllerTest {

    @InjectMocks
    private SellerController sellerController;

    @Mock
    private SellerService sellerService;

    @Mock
    private PersonService personService;

    @Mock
    private UserService userService;

    @Mock
    private DocumentTypeService documentTypeService;

    @Mock
    private APIService apiService;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private HttpServletRequest request;

    private SellerDTO sellerDTO;
    private Seller seller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sellerDTO = new SellerDTO();
        seller = new Seller();
    }

    @Test
    void testGetAllSellersSuccess() {
        when(sellerService.getAllActiveSellers()).thenReturn(List.of(seller));
        ResponseEntity<Object> response = sellerController.getAllSellers();
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetSellerByIdSuccess() {
        String sellerId = "123";
        when(sellerService.existsById(sellerId)).thenReturn(true);
        when(sellerService.getSellerById(sellerId)).thenReturn(Optional.of(seller));

        ResponseEntity<Object> response = sellerController.getSellerById(sellerId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetSellerByIdNotFound() {
        String sellerId = "123";
        when(sellerService.existsById(sellerId)).thenReturn(false);

        ResponseEntity<Object> response = sellerController.getSellerById(sellerId);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Vendedor no encontrado", ((Message) response.getBody()).getMessage());
    }

    @Test
    void testCreateSellerSuccess() {
        when(personService.existsByIdPerson(anyString())).thenReturn(false);
        when(sellerService.saveSeller(any(Seller.class))).thenReturn(seller);
        when(userService.createUser(any(), anyString())).thenReturn(true);

        ResponseEntity<Object> response = sellerController.createSeller(sellerDTO, request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testCreateSellerPersonExists() {
        when(personService.existsByIdPerson(anyString())).thenReturn(true);
        when(personService.getPersonById(anyString())).thenReturn(Optional.of(seller));

        ResponseEntity<Object> response = sellerController.createSeller(sellerDTO, request);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Persona ya existente", ((Message) response.getBody()).getMessage());
    }

    @Test
    void testUpdateSellerSuccess() {
        String sellerId = "123";
        when(sellerService.existsById(sellerId)).thenReturn(true);
        when(sellerService.updateSeller(eq(sellerId), any(Seller.class))).thenReturn(Optional.of(seller));

        ResponseEntity<Object> response = sellerController.updateSeller(sellerId, sellerDTO);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testUpdateSellerNotFound() {
        String sellerId = "123";
        when(sellerService.existsById(sellerId)).thenReturn(false);

        ResponseEntity<Object> response = sellerController.updateSeller(sellerId, sellerDTO);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Este vendedor no existe", ((Message) response.getBody()).getMessage());
    }

    @Test
    void testDeleteSellerSuccess() {
        String sellerId = "123";
        when(sellerService.existsById(sellerId)).thenReturn(true);
        when(sellerService.deleteSeller(eq(sellerId), anyString())).thenReturn(true);
        when(userService.deleteUserAccount(anyString(), anyString())).thenReturn(true);

        ResponseEntity<Object> response = sellerController.deleteSeller(sellerId, request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Vendedor eliminado", ((Message) response.getBody()).getMessage());
    }

    @Test
    void testDeleteSellerNotFound() {
        String sellerId = "123";
        when(sellerService.existsById(sellerId)).thenReturn(false);

        ResponseEntity<Object> response = sellerController.deleteSeller(sellerId, request);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Este vendedor no existe", ((Message) response.getBody()).getMessage());
    }
}
