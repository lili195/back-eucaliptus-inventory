package com.eucaliptus.springboot_app_products.controllers;

import com.eucaliptus.springboot_app_products.dto.Message;
import com.eucaliptus.springboot_app_products.dto.ProductDetailDTO;
import com.eucaliptus.springboot_app_products.mappers.ProductDetailMapper;
import com.eucaliptus.springboot_app_products.model.ProductDetail;
import com.eucaliptus.springboot_app_products.model.Stock;
import com.eucaliptus.springboot_app_products.service.ProductDetailService;
import com.eucaliptus.springboot_app_products.service.ProductService;
import com.eucaliptus.springboot_app_products.service.PurchaseService;
import com.eucaliptus.springboot_app_products.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products/details")
public class ProductDetailController {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductDetailService productDetailService;
    @Autowired
    private StockService stockService;
    @Autowired
    private PurchaseService purchaseService;

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SELLER')")
    public ResponseEntity<List<ProductDetailDTO>> getAllProductDetails() {
        try {
            List<ProductDetailDTO> productDetails = productDetailService.getAllProductDetails().stream()
                    .map(ProductDetailMapper::productDetailToProductDetailDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(productDetails, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getProductDetailById/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SELLER')")
    public ResponseEntity<ProductDetailDTO> getProductDetailById(@PathVariable Long id) {
        try {
            if (!productDetailService.existsById(id)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            Optional<ProductDetail> productDetailOptional = productDetailService.getProductDetailById(id);

            if (productDetailOptional.isPresent()) {
                ProductDetailDTO productDetailDTO = ProductDetailMapper.productDetailToProductDetailDTO(productDetailOptional.get());
                return new ResponseEntity<>(productDetailDTO, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/addProductDetail")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SELLER')")
    public ResponseEntity<Object> createProductDetail(@RequestBody ProductDetailDTO productDetailDTO) {
        try {
            if (!productService.existsByIdProduct(productDetailDTO.getProductDTO().getIdProduct()))
                return new ResponseEntity<>(new Message("Producto no encontrado"),HttpStatus.BAD_REQUEST);
            ProductDetail productDetail = ProductDetailMapper.productDetailDTOToProductDetail(productDetailDTO);
            Stock stock = stockService.getStockByProductId(productDetailDTO.getProductDTO().getIdProduct()).get();
            productDetail.setStock(stock);
            ProductDetail savedProductDetail = productDetailService.saveProductDetail(productDetail);
            return new ResponseEntity<>(ProductDetailMapper.productDetailToProductDetailDTO(savedProductDetail), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/addPurchase")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SELLER')")
    public ResponseEntity<Object> addPurchase(@RequestBody List<ProductDetailDTO> productsDetails) {
        try {
            List<ProductDetailDTO> productsDetailsAdded = purchaseService.addPurchase(productsDetails);
            return new ResponseEntity<>(productsDetailsAdded, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new Message("Intente de nuevo más tarde"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/validatePurchase")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SELLER')")
    public ResponseEntity<Object> validatePurchase(@RequestBody List<ProductDetailDTO> productsDetails) {
        return purchaseService.validatePurchase(productsDetails);
    }


    @PutMapping("/updateProductDetail/{id}")
    public ResponseEntity<ProductDetailDTO> updateProductDetail(@PathVariable Long id, @RequestBody ProductDetailDTO productDetailDTO) {
        try {
            if (!productDetailService.existsById(id)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            ProductDetail productDetail = ProductDetailMapper.productDetailDTOToProductDetail(productDetailDTO);
            productDetail.setIdDetProduct(id);

            Optional<ProductDetail> updatedProductDetailOptional = productDetailService.updateProductDetail(id, productDetail);

            if (updatedProductDetailOptional.isPresent()) {
                ProductDetail updatedProductDetail = updatedProductDetailOptional.get(); // Obtén el valor del Optional
                return new ResponseEntity<>(ProductDetailMapper.productDetailToProductDetailDTO(updatedProductDetail), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @DeleteMapping("/deleteProductDetail/{id}")
    public ResponseEntity<Void> deleteProductDetail(@PathVariable Long id) {
        return productDetailService.deleteProductDetail(id) ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }
}
