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


/**
 * Controlador REST para gestionar los productos de la aplicación.
 *
 * <p>Proporciona endpoints para realizar operaciones CRUD sobre productos,
 * como listar, crear, actualizar y eliminar productos. También incluye
 * funcionalidades específicas como la gestión de productos por proveedor
 * y la consulta de productos próximos a vencer.</p>
 */

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private UnitService unitService;
    @Autowired
    private APIService apiService;

    /**
     * Obtiene todos los productos activos.
     *
     * @return una lista de productos en formato DTO o un mensaje de error si ocurre un problema.
     */

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

    /**
     * Obtiene un producto por su ID.
     *
     * @param id el identificador único del producto.
     * @return el producto en formato DTO o un mensaje de error si no se encuentra.
     */

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

    /**
     * Obtiene múltiples productos por sus IDs.
     *
     * @param ids una lista de identificadores únicos de productos.
     * @return una lista de productos en formato DTO o un mensaje de error si no se encuentran.
     */

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

    /**
     * Obtiene un producto por su nombre.
     *
     * @param productName el nombre del producto.
     * @return el producto en formato DTO o un mensaje de error si no se encuentra.
     */

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

    /**
     * Obtiene productos asociados a un proveedor específico.
     *
     * @param providerId el identificador del proveedor.
     * @return una lista de productos en formato DTO.
     */

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

    /**
     * Crea un nuevo producto.
     *
     * @param productDTO los datos del producto en formato DTO.
     * @param request    el objeto HttpServletRequest para obtener información adicional.
     * @return el producto creado en formato DTO o un mensaje de error.
     */

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

    /**
     * Actualiza un producto existente.
     *
     * @param idProduct  el identificador del producto a actualizar.
     * @param productDTO los nuevos datos del producto en formato DTO.
     * @param request    el objeto HttpServletRequest para obtener información adicional.
     * @return el producto actualizado en formato DTO o un mensaje de error.
     */

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

    /**
     * Elimina un producto por su ID.
     *
     * @param idProduct el identificador del producto.
     * @param request   el objeto HttpServletRequest para obtener información adicional.
     * @return un mensaje de éxito o error.
     */

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
