package com.eucaliptus.springboot_app_products.controllers;

import com.eucaliptus.springboot_app_products.dto.ProductDetailDTO;
import com.eucaliptus.springboot_app_products.mappers.ProductDetailMapper;
import com.eucaliptus.springboot_app_products.model.ProductDetail;
import com.eucaliptus.springboot_app_products.service.ProductDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products/details")
public class ProductDetailController {

    @Autowired
    private ProductDetailService productDetailService;

    @GetMapping("/all")
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
    public ResponseEntity<ProductDetailDTO> getProductDetailById(@PathVariable Long id) {
        try {
            if (!productDetailService.existsById(id)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // Obtén el Optional<ProductDetail> del servicio
            Optional<ProductDetail> productDetailOptional = productDetailService.getProductDetailById(id);

            // Verifica si el Optional contiene un valor
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
    public ResponseEntity<ProductDetailDTO> createProductDetail(@RequestBody ProductDetailDTO productDetailDTO) {
        try {
            ProductDetail productDetail = ProductDetailMapper.productDetailDTOToProductDetail(productDetailDTO);
            ProductDetail savedProductDetail = productDetailService.saveProductDetail(productDetail);
            return new ResponseEntity<>(ProductDetailMapper.productDetailToProductDetailDTO(savedProductDetail), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updateProductDetail/{id}")
    public ResponseEntity<ProductDetailDTO> updateProductDetail(@PathVariable Long id, @RequestBody ProductDetailDTO productDetailDTO) {
        try {
            if (!productDetailService.existsById(id)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            ProductDetail productDetail = ProductDetailMapper.productDetailDTOToProductDetail(productDetailDTO);
            productDetail.setIdDetProduct(id); // Asigna el ID al objeto ProductDetail

            // Llama al método que espera el ID y el objeto ProductDetail
            Optional<ProductDetail> updatedProductDetailOptional = productDetailService.updateProductDetail(id, productDetail);

            // Verifica si el Optional contiene un valor
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
