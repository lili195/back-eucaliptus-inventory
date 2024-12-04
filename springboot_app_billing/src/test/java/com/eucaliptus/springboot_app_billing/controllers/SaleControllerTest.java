package com.eucaliptus.springboot_app_billing.controllers;

import com.eucaliptus.springboot_app_billing.dto.*;
import com.eucaliptus.springboot_app_billing.model.Client;
import com.eucaliptus.springboot_app_billing.model.Sale;
import com.eucaliptus.springboot_app_billing.model.Summary;
import com.eucaliptus.springboot_app_billing.service.*;
import com.eucaliptus.springboot_app_billing.mappers.SaleMapper;
import com.eucaliptus.springboot_app_billing.mappers.SaleDetailMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpServletRequest;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SaleControllerTest {

    @InjectMocks
    private SaleController saleController;

    @Mock
    private SaleService saleService;

    @Mock
    private SaleDetailService saleDetailService;

    @Mock
    private ProductService productService;

    @Mock
    private SummaryService summaryService;

    @Mock
    private APIService apiService;

    @Mock
    private SendBillService sendBillService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private RestTemplate restTemplate;

    private SaleDTO saleDTO;
    private List<SaleDetailDTO> saleDetails;
    private Optional<Sale> saleOptional;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        saleDTO = new SaleDTO();
        saleDTO.setIdSale(1);
        saleDTO.setIdSeller("seller123");
        saleDTO.setTotal(100.0);

        Sale sale = new Sale();
        sale.setIdSale(1);
        sale.setIdSeller("seller123");
        sale.setTotalSale(100.0);
        sale.setSaleDate(new Date());
        sale.setClient(new Client()); // Mocked client

        saleOptional = Optional.of(sale);

        saleDetails = new ArrayList<>();
        saleDetails.add(new SaleDetailDTO());
    }

    @Test
    void addSale_Success() {
        when(saleService.addSale(any(), eq(request))).thenReturn(new Sale());
        when(productService.getSaleDetails(anyList(), anyString())).thenReturn(saleDetails);

        ResponseEntity<Object> response = saleController.addSale(saleDTO, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void addSale_Failure_IllegalArgument() {
        when(saleService.addSale(any(), eq(request))).thenThrow(new IllegalArgumentException("Vendedor no encontrado"));

        ResponseEntity<Object> response = saleController.addSale(saleDTO, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Vendedor no encontrado"));
    }

    @Test
    void getHistorySale_Success() {
        DatesDTO datesDTO = new DatesDTO(new Date(), new Date());
        datesDTO.setStartDate(new Date());


        ResponseEntity<Object> response = saleController.getHistorySale(datesDTO, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(((List<?>) response.getBody()).size() > 0);
    }

    @Test
    void getHistorySale_Failure() {
        DatesDTO datesDTO = new DatesDTO(new Date(), new Date());
        datesDTO.setStartDate(new Date());

        when(saleService.getSalesByDate(any())).thenThrow(new RuntimeException("Error fetching sales"));

        ResponseEntity<Object> response = saleController.getHistorySale(datesDTO, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void getSaleDetails_Success() {
        when(saleService.getSaleById(anyInt())).thenReturn(saleOptional);
        when(productService.getSaleDetails(anyList(), anyString())).thenReturn(saleDetails);

        ResponseEntity<Object> response = saleController.getSaleDetails(1, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getSaleDetails_Failure_SaleNotFound() {
        when(saleService.getSaleById(anyInt())).thenReturn(Optional.empty());

        ResponseEntity<Object> response = saleController.getSaleDetails(1, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Venta no encontrada"));
    }

    @Test
    void getProductsSale_Success() {
        DatesDTO datesDTO = new DatesDTO(new Date(), new Date());
        datesDTO.setStartDate(new Date());
        datesDTO.setEndDate(new Date());

        ResponseEntity<Object> response = saleController.getProductsSale(datesDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void sendEmail_Success() {
        when(saleService.getSaleById(anyInt())).thenReturn(saleOptional);
        when(productService.getSaleDetails(anyList(), anyString())).thenReturn(saleDetails);
        doNothing().when(sendBillService).sendSale(anyString(), any());

        ResponseEntity<Object> response = saleController.sendEmail(1, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void sendEmail_Failure_SaleNotFound() {
        when(saleService.getSaleById(anyInt())).thenReturn(Optional.empty());

        ResponseEntity<Object> response = saleController.sendEmail(1, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Venta no encontrada"));
    }

    @Test
    void getGlobalSummary_Success() {
        Optional<Summary> summary = Optional.of(new Summary());
        when(summaryService.getGlobalSummary()).thenReturn(summary);

        ResponseEntity<Object> response = saleController.getGlobalSummary();

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getGlobalSummary_Failure() {
        when(summaryService.getGlobalSummary()).thenReturn(Optional.empty());

        ResponseEntity<Object> response = saleController.getGlobalSummary();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
