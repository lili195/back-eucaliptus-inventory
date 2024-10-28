package com.eucaliptus.springboot_app_products.controllers;

import com.eucaliptus.springboot_app_products.dto.Message;
import com.eucaliptus.springboot_app_products.dto.ProductDTO;
import com.eucaliptus.springboot_app_products.dto.UpdateProductDTO;
import com.eucaliptus.springboot_app_products.enums.EnumCategory;
import com.eucaliptus.springboot_app_products.enums.EnumUse;
import com.eucaliptus.springboot_app_products.mappers.ProductMapper;
import com.eucaliptus.springboot_app_products.mappers.UpdateProductMapper;
import com.eucaliptus.springboot_app_products.model.Product;
import com.eucaliptus.springboot_app_products.model.Unit;
import com.eucaliptus.springboot_app_products.security.JwtTokenUtil;
import com.eucaliptus.springboot_app_products.service.ProductService;
import com.eucaliptus.springboot_app_products.service.UnitService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;
    private UnitService unitService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> getAllProducts() {
        try {
            return new ResponseEntity<>(productService.getAllActiveProducts().stream().
                    map(ProductMapper::productToProductDTO).collect(Collectors.toList()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new com.eucaliptus.springboot_app_products.dto.Message("Intente de nuevo mas tarde"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getProductById/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> getProductById(@PathVariable Long id) {
        try {
            if (!productService.existsByIdProduct(id))
                return new ResponseEntity<>(new Message("Producto no encontrado"), HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(ProductMapper.productToProductDTO(productService.getProductById(id).get()), HttpStatus.OK);
            } catch (Exception e){
                return new ResponseEntity<>(new com.eucaliptus.springboot_app_products.dto.Message("Intentalo mas tarde"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
    }

    @GetMapping("getProductByUsername/{productName}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SELLER')")
    public ResponseEntity<Object> getProductByUsername(@PathVariable String productName) {
        try{
            if(!productService.existsByNameProduct(productName))
                return new ResponseEntity<>(new Message("Producto no encontrado"), HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(ProductMapper.productToProductDTO(productService.getProductByName(productName).get()), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(new com.eucaliptus.springboot_app_products.dto.Message("Intentalo mas tarde"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/addProduct")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> createProduct(@RequestBody ProductDTO productDTO, HttpServletRequest request) {
        try {
            long existId = 0;
            if (productService.existsByIdProduct(productDTO.getIdProduct()) || productService.existsByNameProduct(productDTO.getProductName())) {
                Optional<Product> opProduct = productService.getProductById(productDTO.getIdProduct());
                if (opProduct.isPresent() && opProduct.get().isActive())
                    return new ResponseEntity<>(new Message("Producto ya existente"), HttpStatus.BAD_REQUEST);

                if (opProduct.isPresent())
                    existId = opProduct.get().getIdProduct();
            }
            EnumCategory category = productDTO.getCategory();
            EnumUse use = productDTO.getUse();

            Unit idUnit;
            if (unitService.existsByIdUnit(productDTO.getIdUnit().getIdUnit())) {
                idUnit = unitService.getUnitById(productDTO.getIdUnit().getIdUnit()).get();
            } else {
                return new ResponseEntity<>(new Message("Unidad no encontrada"), HttpStatus.BAD_REQUEST);
            }

            Product product = ProductMapper.productDTOToProduct(productDTO, idUnit);
            product.setCategory(category);
            product.setUse(use);

            if (existId != 0)
                product.setIdProduct(existId);

            if (!productService.createProduct(productDTO, productService.getTokenByRequest(request))) {
                return new ResponseEntity<>(new Message("Intente de nuevo más tarde"), HttpStatus.INTERNAL_SERVER_ERROR);
            }

            // Corrige aquí: pasa el producto creado y el token
            return new ResponseEntity<>(ProductMapper.productToProductDTO(product), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new Message("Intente de nuevo más tarde"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updateProduct/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> updateProduct(@PathVariable("id") int idProduct, @RequestBody UpdateProductDTO updateProductDTO, HttpServletRequest request) {
        try {
            if (!productService.existsByIdProduct((long) idProduct)) {
                return new ResponseEntity<>(new Message("Producto no encontrado"), HttpStatus.BAD_REQUEST);
            }

            Optional<Unit> unitOptional = unitService.getUnitById(updateProductDTO.getIdUnit().getIdUnit());
            if (unitOptional.isEmpty()) {
                return new ResponseEntity<>(new Message("Unidad no encontrada"), HttpStatus.BAD_REQUEST);
            }
            int unit = unitOptional.get().getIdUnit();

            Product existingProduct = productService.getProductById((long) idProduct)
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado")); // Lanzar excepción si no se encuentra el producto

            // Actualizar los detalles del producto existente
            UpdateProductMapper.updateProductDTOToProduct(updateProductDTO, existingProduct);

            // Crear un DTO para pasar al método de servicio
            UpdateProductDTO updatedProductDTO = UpdateProductMapper.productToUpdateProductDTO(existingProduct);

            if (!productService.updateProduct(updatedProductDTO, productService.getTokenByRequest(request))) {
                return new ResponseEntity<>(new Message("Error al actualizar el producto"), HttpStatus.INTERNAL_SERVER_ERROR);
            }

            return new ResponseEntity<>(updatedProductDTO, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new Message("Intente de nuevo más tarde"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/deleteProduct/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> deleteProduct(@PathVariable("id") Long idProduct, HttpServletRequest request) {
        try {
            if (!productService.existsByIdProduct(idProduct)) {
                return new ResponseEntity<>(new Message("Producto no encontrado"), HttpStatus.BAD_REQUEST);
            }

            String productName = productService.getProductById(idProduct).get().getName();
            String token = productService.getTokenByRequest(request);

            if (productService.deleteProduct(productName, token)) {
                return new ResponseEntity<>(new Message("Producto eliminado exitosamente"), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new Message("Error con la base de datos"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new Message("Intente de nuevo más tarde"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
