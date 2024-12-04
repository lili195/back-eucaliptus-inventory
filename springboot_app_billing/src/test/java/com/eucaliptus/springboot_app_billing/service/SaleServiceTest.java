package com.eucaliptus.springboot_app_billing.service;

import com.eucaliptus.springboot_app_billing.dto.*;
import com.eucaliptus.springboot_app_billing.model.Client;
import com.eucaliptus.springboot_app_billing.model.Sale;
import com.eucaliptus.springboot_app_billing.model.SaleDetail;
import com.eucaliptus.springboot_app_billing.repository.SaleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class SaleServiceTest {

    @Mock
    private SaleRepository saleRepository;
    @Mock
    private ClientService clientService;
    @Mock
    private SaleDetailService saleDetailService;
    @Mock
    private APIService apiService;
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private SaleService saleService;

    private SaleDTO saleDTO;
    private Client client;
    private Sale sale;
    private SaleDetail saleDetail;

    @BeforeEach
    public void setUp() {
        // Configura los objetos necesarios
        saleDTO = new SaleDTO();
        saleDTO.setTotal(350.0);

        client = new Client();
        client.setIdClient("123");
        saleDTO.setClientDTO(new ClientDTO());
        saleDTO.getClientDTO().setIdClient("123");

        sale = new Sale();
        sale.setIdSale(1);
        sale.setTotalSale(350.0);

        saleDetail = new SaleDetail();
        saleDetail.setSale(sale);
        saleDetail.setQuantitySold(2);
        saleDetail.setSalePrice(50.0);

        when(apiService.getTokenByRequest(any())).thenReturn("some-token");
        when(clientService.saveClient(any())).thenReturn(client);
        when(saleRepository.save(any())).thenReturn(sale);
        when(saleDetailService.saveSaleDetail(any())).thenReturn(saleDetail);
    }

    // Prueba para el método getSalesByDate
    @Test
    public void testGetSalesByDate() {
        Date date = new Date();
        when(saleRepository.findBySaleDate(date)).thenReturn(List.of(sale));

        List<Sale> result = saleService.getSalesByDate(date);
        assertEquals(1, result.size());
        assertEquals(350.0, result.get(0).getTotalSale());
    }

    // Prueba para el método getSaleById
    @Test
    public void testGetSaleById() {
        when(saleRepository.findById(1)).thenReturn(Optional.of(sale));

        Optional<Sale> result = saleService.getSaleById(1);
        assertTrue(result.isPresent());
        assertEquals(350.0, result.get().getTotalSale());
    }

    // Prueba para el método saveBill
    @Test
    public void testSaveBill() {
        when(saleRepository.save(sale)).thenReturn(sale);

        Sale result = saleService.saveBill(sale);
        assertNotNull(result);
        assertEquals(350.0, result.getTotalSale());
    }

    // Prueba para el método addSale
    @Test
    public void testAddSale() {
        // Simula la llamada al método addSale
        Sale result = saleService.addSale(saleDTO, null);

        // Verifica los resultados
        assertNotNull(result);
        assertEquals(350.0, result.getTotalSale());
        verify(saleRepository, times(1)).save(any(Sale.class));
        verify(saleDetailService, times(1)).saveSaleDetail(any(SaleDetail.class));
    }

    // Prueba para el método addSale cuando no se encuentra un vendedor
    @Test
    public void testAddSaleThrowsExceptionWhenNoSellerFound() {
        // Configura la situación donde no se encuentra un vendedor
        when(apiService.getTokenByRequest(any())).thenReturn("");

        // Verifica que se lance la excepción esperada
        assertThrows(IllegalArgumentException.class, () -> saleService.addSale(saleDTO, null));
    }

}
