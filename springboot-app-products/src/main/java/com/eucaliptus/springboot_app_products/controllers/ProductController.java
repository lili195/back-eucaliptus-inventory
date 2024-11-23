package com.eucaliptus.springboot_app_products.controllers;

import com.eucaliptus.springboot_app_products.dto.Message;
import com.eucaliptus.springboot_app_products.dto.ProductDTO;
import com.eucaliptus.springboot_app_products.dto.ProductExpiringSoonDTO;
import com.eucaliptus.springboot_app_products.dto.UnitDTO;
import com.eucaliptus.springboot_app_products.mappers.ProductMapper;
import com.eucaliptus.springboot_app_products.mappers.UnitMapper;
import com.eucaliptus.springboot_app_products.model.Product;
import com.eucaliptus.springboot_app_products.model.Stock;
import com.eucaliptus.springboot_app_products.model.Unit;
import com.eucaliptus.springboot_app_products.service.APIService;
import com.eucaliptus.springboot_app_products.service.ProductService;
import com.eucaliptus.springboot_app_products.service.StockService;
import com.eucaliptus.springboot_app_products.service.UnitService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private UnitService unitService;
    @Autowired
    private APIService apiService;

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SELLER')")
    public ResponseEntity<Object> getAllProducts() {
        try {
            return new ResponseEntity<>(productService.getAllActiveProducts().stream().
                    map(ProductMapper::productToProductDTO).collect(Collectors.toList()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Message("Intente de nuevo mas tarde"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getProductById/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SELLER')")
    public ResponseEntity<Object> getProductById(@PathVariable String id) {
        try {
            if (!productService.existsByIdProduct(id))
                return new ResponseEntity<>(new Message("Producto no encontrado"), HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(ProductMapper.productToProductDTO(productService.getProductById(id).get()), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(new Message("Intentalo mas tarde"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/getProductsById")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SELLER')")
    public ResponseEntity<Object> getProductsById(@RequestBody List<String> ids) {
        try {
            List<ProductDTO> productDTOS = new ArrayList<>();
            for (String id : ids) {
                Optional<Product> opProduct = productService.getProductById(id);
                if (opProduct.isEmpty())
                    return new ResponseEntity<>(new Message("Producto no encontrado"), HttpStatus.BAD_REQUEST);
                productDTOS.add(ProductMapper.productToProductDTO(opProduct.get()));
            }
            return new ResponseEntity<>(productDTOS, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(new Message("Intentalo mas tarde"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("getProductByName/{productName}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SELLER')")
    public ResponseEntity<Object> getProductByName(@PathVariable String productName) {
        try{
            if(!productService.existsByNameProduct(productName))
                return new ResponseEntity<>(new Message("Producto no encontrado"), HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(ProductMapper.productToProductDTO(productService.getProductByName(productName).get()), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(new Message("Intentalo mas tarde"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("getProductsByProvider/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SELLER')")
    public ResponseEntity<Object> getProductsByProvider(@PathVariable("id") String providerId) {
        try{
            return new ResponseEntity<>(productService.getProductsByIdProvider(providerId).stream().
                    map(ProductMapper::productToProductDTO).collect(Collectors.toList()), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(new Message("Intentalo mas tarde"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/addProduct")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SELLER')")
    public ResponseEntity<Object> createProduct(@RequestBody ProductDTO productDTO, HttpServletRequest request) {
        try {
            if (!productService.existsProviderId(productDTO.getIdProvider(), apiService.getTokenByRequest(request)))
                return new ResponseEntity<>(new Message("Proveedor no existente"), HttpStatus.BAD_REQUEST);
            if (productService.existsByIdProduct(productDTO.getIdProduct()))
                return new ResponseEntity<>(new Message("Id de producto ya existente"), HttpStatus.BAD_REQUEST);
            UnitDTO unitDTO = productDTO.getUnitDTO();
            Optional<Unit> opUnit = unitService.getUnitByNameAndDescription(unitDTO.getUnitName(), unitDTO.getDescription());
            Unit unit = (opUnit.isPresent()) ?
                    opUnit.get() :
                    unitService.saveUnit(UnitMapper.unitDTOToUnit(unitDTO));

            Product product = ProductMapper.productDTOToProduct(productDTO, unit);
            product = productService.saveProduct(product);
            return new ResponseEntity<>(ProductMapper.productToProductDTO(product), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new Message("Intente de nuevo más tarde"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updateProduct/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SELLER')")
    public ResponseEntity<Object> updateProduct(@PathVariable("id") String idProduct, @RequestBody ProductDTO productDTO, HttpServletRequest request) {
        try {
            if (!productService.existsByIdProduct(idProduct))
                return new ResponseEntity<>(new Message("Producto no encontrado"), HttpStatus.BAD_REQUEST);

            UnitDTO unitDTO = productDTO.getUnitDTO();
            Optional<Unit> opUnit = unitService.getUnitByNameAndDescription(unitDTO.getUnitName(), unitDTO.getDescription());
            Unit unit = (opUnit.isPresent()) ?
                    opUnit.get() :
                    unitService.saveUnit(UnitMapper.unitDTOToUnit(unitDTO));

            Product product = ProductMapper.productDTOToProduct(productDTO, unit);
            product = productService.updateProduct(idProduct, product).get();

            return new ResponseEntity<>(ProductMapper.productToProductDTO(product), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new Message("Intente de nuevo más tarde"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/deleteProduct/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SELLER')")
    public ResponseEntity<Object> deleteProduct(@PathVariable("id") String idProduct, HttpServletRequest request) {
        try {
            if (!productService.existsByIdProduct(idProduct))
                return new ResponseEntity<>(new Message("Producto no encontrado"), HttpStatus.BAD_REQUEST);
            if (productService.deleteProduct(idProduct))
                return new ResponseEntity<>(new Message("Producto eliminado exitosamente"), HttpStatus.OK);
            return new ResponseEntity<>(new Message("Error con la base de datos"), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new Message("Intente de nuevo más tarde"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteProductsByProvider/{idProvider}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SELLER')")
    public ResponseEntity<Object> deleteProductsByProvider(@PathVariable("idProvider") String idProvider) {
        try {
            productService.deleteProductsByIdProvider(idProvider);
            return new ResponseEntity<>(new Message("Producto eliminado exitosamente"), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new Message("Intente de nuevo más tarde"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/expiring-soon")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SELLER')")
    public List<ProductExpiringSoonDTO> getProductsExpiringSoon() {
        return productService.getProductsExpiringSoon();
    }

}
