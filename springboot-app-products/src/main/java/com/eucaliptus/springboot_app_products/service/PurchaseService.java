package com.eucaliptus.springboot_app_products.service;

import com.eucaliptus.springboot_app_products.dto.Message;
import com.eucaliptus.springboot_app_products.dto.ProductDetailDTO;
import com.eucaliptus.springboot_app_products.model.Batch;
import com.eucaliptus.springboot_app_products.model.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PurchaseService {

    @Autowired
    private ProductService productService;
    @Autowired
    private StockService stockService;
    @Autowired
    private BatchService batchService;

//    @Transactional
//    public List<ProductDetailDTO> addPurchase(List<ProductDetailDTO> productsDetails) throws Exception {
//        List<ProductDetailDTO> productsDetailsAdded = new ArrayList<>();
//        for (ProductDetailDTO productDetailDTO : productsDetails) {
//            if (!productService.existsByIdProduct(productDetailDTO.getProductDTO().getIdProduct())) {
//                throw new IllegalArgumentException("Producto no encontrado: " + productDetailDTO.getProductDTO().getIdProduct());
//            }
//            Batch batch = ProductDetailMapper.productDetailDTOToProductDetail(productDetailDTO);
//            Stock stock = stockService.getStockByProductId(productDetailDTO.getProductDTO().getIdProduct())
//                    .orElseThrow(() -> new IllegalArgumentException("Stock no encontrado para el producto: " + productDetailDTO.getProductDTO().getIdProduct()));
//            batch.setStock(stock);
//            Batch savedBatch = batchService.saveBatch(batch);
//            productsDetailsAdded.add(ProductDetailMapper.productDetailToProductDetailDTO(savedBatch));
//        }
//        return productsDetailsAdded;
//    }
//
//    public ResponseEntity<Object> validatePurchase(List<ProductDetailDTO> productsDetails)  {
//        String id = "";
//        if (!(id = allProductsExist(productsDetails)).isEmpty())
//            return new ResponseEntity<>(new Message("Producto no encontrado: " + id), HttpStatus.NOT_FOUND);
//        if (existByProductIdAndBatch(productsDetails.get(productsDetails.size()-1)))
//            return new ResponseEntity<>(new Message("Producto (" + productsDetails.get(productsDetails.size()-1).getProductDTO().getIdProduct() + ") y lote (" + productsDetails.get(productsDetails.size()-1).getBatch()+") ya existente en la bd"), HttpStatus.CONFLICT);
//        if (thereAreRepeated(productsDetails))
//            return new ResponseEntity<>(new Message("Este producto con este lote ya fue añadido a la lista"), HttpStatus.BAD_REQUEST);
//        return new ResponseEntity<>(new Message("Compra valida"), HttpStatus.OK);
//    }
//
//    private String allProductsExist(List<ProductDetailDTO> productsDetails){
//        for (ProductDetailDTO productDetailDTO : productsDetails)
//            if (!productService.existsByIdProduct(productDetailDTO.getProductDTO().getIdProduct()))
//                return productDetailDTO.getProductDTO().getIdProduct();
//        return "";
//    }
//
//    private boolean existByProductIdAndBatch(ProductDetailDTO productDetailDTO) {
//        return batchService.existsByIdProductAndBatch(productDetailDTO.getProductDTO().getIdProduct(), productDetailDTO.getBatch());
//    }
//
//    private boolean thereAreRepeated(List<ProductDetailDTO> productsDetails) {
//        Set<String> setProductId = new HashSet<>();
//        for (ProductDetailDTO productDetailDTO : productsDetails)
//            if (!setProductId.add(productDetailDTO.getProductDTO().getIdProduct()+productDetailDTO.getBatch()))
//                return true;
//        return false;
//    }

}
