package com.eucaliptus.springboot_app_products.service;

import com.eucaliptus.springboot_app_products.dto.ProductDetailDTO;
import com.eucaliptus.springboot_app_products.mappers.ProductDetailMapper;
import com.eucaliptus.springboot_app_products.model.ProductDetail;
import com.eucaliptus.springboot_app_products.model.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PurchaseService {

    @Autowired
    private ProductService productService;
    @Autowired
    private StockService stockService;
    @Autowired
    private ProductDetailService productDetailService;

    @Transactional
    public List<ProductDetailDTO> addPurchase(List<ProductDetailDTO> productsDetails) throws Exception {
        List<ProductDetailDTO> productsDetailsAdded = new ArrayList<>();
        for (ProductDetailDTO productDetailDTO : productsDetails) {
            if (!productService.existsByIdProduct(productDetailDTO.getProductDTO().getIdProduct())) {
                throw new IllegalArgumentException("Producto no encontrado: " + productDetailDTO.getProductDTO().getIdProduct());
            }
            ProductDetail productDetail = ProductDetailMapper.productDetailDTOToProductDetail(productDetailDTO);
            Stock stock = stockService.getStockByProductId(productDetailDTO.getProductDTO().getIdProduct())
                    .orElseThrow(() -> new IllegalArgumentException("Stock no encontrado para el producto: " + productDetailDTO.getProductDTO().getIdProduct()));
            productDetail.setStock(stock);
            ProductDetail savedProductDetail = productDetailService.saveProductDetail(productDetail);
            productsDetailsAdded.add(ProductDetailMapper.productDetailToProductDetailDTO(savedProductDetail));
        }
        return productsDetailsAdded;
    }

}
