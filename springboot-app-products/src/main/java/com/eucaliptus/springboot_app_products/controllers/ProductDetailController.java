package com.eucaliptus.springboot_app_products.controllers;

import com.eucaliptus.springboot_app_products.dto.Message;
import com.eucaliptus.springboot_app_products.dto.ProductDetailDTO;
import com.eucaliptus.springboot_app_products.model.Batch;
import com.eucaliptus.springboot_app_products.service.BatchService;
import com.eucaliptus.springboot_app_products.service.ProductService;
import com.eucaliptus.springboot_app_products.service.PurchaseService;
import com.eucaliptus.springboot_app_products.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products/details")
public class ProductDetailController {

    @Autowired
    private ProductService productService;
    @Autowired
    private BatchService batchService;
    @Autowired
    private StockService stockService;
    @Autowired
    private PurchaseService purchaseService;

//    @GetMapping("/all")
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SELLER')")
//    public ResponseEntity<List<ProductDetailDTO>> getAllProductDetails() {
//        try {
//            List<ProductDetailDTO> productDetails = batchService.getAllBatches().stream()
//                    .map(ProductDetailMapper::productDetailToProductDetailDTO)
//                    .collect(Collectors.toList());
//            return new ResponseEntity<>(productDetails, HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    @PostMapping("/addProductDetail")
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SELLER')")
//    public ResponseEntity<Object> createProductDetail(@RequestBody ProductDetailDTO productDetailDTO) {
//        return null;
//    }
//
//    @PostMapping("/addPurchase")
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SELLER')")
//    public ResponseEntity<Object> addPurchase(@RequestBody List<ProductDetailDTO> productsDetails) {
//        try {
//            List<ProductDetailDTO> productsDetailsAdded = purchaseService.addPurchase(productsDetails);
//            return new ResponseEntity<>(productsDetailsAdded, HttpStatus.CREATED);
//        } catch (IllegalArgumentException e) {
//            return new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.BAD_REQUEST);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new ResponseEntity<>(new Message("Intente de nuevo más tarde"), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    @PostMapping("/validatePurchase")
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SELLER')")
//    public ResponseEntity<Object> validatePurchase(@RequestBody List<ProductDetailDTO> productsDetails) {
//        return purchaseService.validatePurchase(productsDetails);
//    }
//
//
//    @PutMapping("/updateProductDetail/{id}")
//    public ResponseEntity<ProductDetailDTO> updateProductDetail(@PathVariable Long id, @RequestBody ProductDetailDTO productDetailDTO) {
//        try {
//            if (!batchService.existsById(id)) {
//                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//            }
//            Batch batch = ProductDetailMapper.productDetailDTOToProductDetail(productDetailDTO);
//            batch.setIdDetProduct(id);
//
//            Optional<Batch> updatedProductDetailOptional = batchService.updateProductDetail(id, batch);
//
//            if (updatedProductDetailOptional.isPresent()) {
//                Batch updatedBatch = updatedProductDetailOptional.get(); // Obtén el valor del Optional
//                return new ResponseEntity<>(ProductDetailMapper.productDetailToProductDetailDTO(updatedBatch), HttpStatus.OK);
//            } else {
//                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//            }
//        } catch (Exception e) {
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//
//
//    @DeleteMapping("/deleteProductDetail/{id}")
//    public ResponseEntity<Void> deleteProductDetail(@PathVariable Long id) {
//        return batchService.deleteProductDetail(id) ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
//    }
}
