package com.eucaliptus.springboot_app_products.controllers;

import com.eucaliptus.springboot_app_products.dto.ProductDTO;
import com.eucaliptus.springboot_app_products.mappers.ProductMapper;
import com.eucaliptus.springboot_app_products.model.Product;
import com.eucaliptus.springboot_app_products.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/all")
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        try {
            List<ProductDTO> products = productService.getAllProducts().stream()
                    .map(ProductMapper::productToProductDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(products, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getProductById/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        try {
            if (!productService.existsById(id)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // Obtén el Optional<Product> del servicio
            Optional<Product> productOptional = productService.getProductById(id);

            // Verifica si el Optional contiene un valor
            if (productOptional.isPresent()) {
                ProductDTO productDTO = ProductMapper.productToProductDTO(productOptional.get());
                return new ResponseEntity<>(productDTO, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/addProduct")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
        try {
            Product product = ProductMapper.productDTOToProduct(productDTO);
            Product savedProduct = productService.saveProduct(product);
            return new ResponseEntity<>(ProductMapper.productToProductDTO(savedProduct), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updateProduct/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        try {
            if (!productService.existsById(id)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            Product product = ProductMapper.productDTOToProduct(productDTO);
            product.setIdProduct(id); // Asigna el ID al objeto Product

            // El método updateProduct devuelve un Optional<Product>
            Optional<Product> updatedProductOptional = productService.updateProduct(id, product);

            // Verifica si el Optional contiene un valor
            if (updatedProductOptional.isPresent()) {
                Product updatedProduct = updatedProductOptional.get(); // Obtén el valor del Optional
                return new ResponseEntity<>(ProductMapper.productToProductDTO(updatedProduct), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteProduct/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        return productService.deleteProduct(id) ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }
}
