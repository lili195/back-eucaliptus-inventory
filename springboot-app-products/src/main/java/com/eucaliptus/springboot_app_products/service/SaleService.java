package com.eucaliptus.springboot_app_products.service;

import com.eucaliptus.springboot_app_products.dto.NewBatchDTO;
import com.eucaliptus.springboot_app_products.dto.RequestBatchDTO;
import com.eucaliptus.springboot_app_products.dto.SaleDetailDTO;
import com.eucaliptus.springboot_app_products.mappers.StockMapper;
import com.eucaliptus.springboot_app_products.model.Batch;
import com.eucaliptus.springboot_app_products.model.Product;
import com.eucaliptus.springboot_app_products.model.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SaleService {

    @Autowired
    private StockService stockService;
    @Autowired
    private BatchService batchService;
    @Autowired
    private ProductService productService;

    public List<SaleDetailDTO> getSaleDetails(List<RequestBatchDTO> requests){
        ArrayList<SaleDetailDTO> saleDetails = new ArrayList<>();
        for (RequestBatchDTO request : requests) {
            String id = "";
            if (!(id = allProductsExist(requests)).isEmpty())
                throw new IllegalArgumentException("Producto no encontrado: " + request.getIdProduct());
            Optional<Stock> opStock = stockService.getStockByProductId(request.getIdProduct());
            if (opStock.isEmpty())
                throw new IllegalArgumentException("Sin unidades disponibles: " + request.getIdProduct());
            if (opStock.get().getQuantityAvailable() < request.getQuantity())
                throw new IllegalArgumentException("Sin unidades suficientes: " + request.getIdProduct());
            Stock stock = opStock.get();
            int quantityRequired = request.getQuantity();
            List<Batch> batches = batchService.getFirstAvailableBatch(request.getIdProduct());
            saleDetails.addAll(getSaleDetailsByProduct(batches, stock, quantityRequired));
        }
        return saleDetails;
    }

    private List<SaleDetailDTO> getSaleDetailsByProduct(List<Batch> batches, Stock stock, int quantityRequired){
        ArrayList<SaleDetailDTO> saleDetails = new ArrayList<>();
        int i = 0;
        while (quantityRequired > 0 && i < batches.size()) {
            SaleDetailDTO saleDetail = new SaleDetailDTO();
            saleDetail.setIdProduct(batches.get(i).getIdProduct());
            saleDetail.setBatch(batches.get(i).getBatch());
            int quantityAdded = (quantityRequired <= batches.get(i).getQuantityAvailableBatch()) ?
                    quantityRequired : batches.get(i).getQuantityAvailableBatch();
            saleDetail.setQuantitySold(quantityAdded);
            saleDetail.setSalePrice(stock.getProductSalePrice());
            saleDetail.setSalePriceWithoutIva(stock.getProductSalePriceWithoutIva());
            saleDetails.add(saleDetail);
            quantityRequired -= quantityAdded;
            i++;
        }
        return saleDetails;
    }

    @Transactional
    public boolean updateStock(List<SaleDetailDTO> saleDetails){
        for (SaleDetailDTO saleDetail : saleDetails) {
            Optional<Product> opProduct = productService.getProductById(saleDetail.getIdProduct());
            if (opProduct.isEmpty())
                throw new IllegalArgumentException("Producto no encontrado: " + saleDetail.getIdProduct());
            Stock stock = StockMapper.SaleDetailDTOToStock(saleDetail);
            stock.setProduct(opProduct.get());
            Optional<Stock> existStock = stockService.getStockByProductId(saleDetail.getIdProduct());
            int quantity = (existStock.isEmpty()) ?
                    saleDetail.getQuantitySold():
                    existStock.get().getQuantityAvailable() - saleDetail.getQuantitySold();
            stock.setQuantityAvailable(quantity);
            stockService.saveStock(stock);
        }
        if (!updateBatch(saleDetails))
            return false;
        return true;
    }

    @Transactional
    public boolean updateBatch(List<SaleDetailDTO> saleDetails){
        for (SaleDetailDTO saleDetail : saleDetails) {
            Optional<Product> opProduct = productService.getProductById(saleDetail.getIdProduct());
            if (opProduct.isEmpty())
                throw new IllegalArgumentException("Producto no encontrado: " + saleDetail.getIdProduct());
            System.out.println(saleDetail.getIdProduct() + " - " + saleDetail.getBatch());
            Batch batch = batchService.findByIdProductAndBatch(saleDetail.getIdProduct(), saleDetail.getBatch()).get();
            batchService.updateQuantityAvailable(saleDetail.getIdProduct(), saleDetail.getBatch(), batch.getQuantityAvailableBatch() - saleDetail.getQuantitySold());
        }
        return true;
    }

    private String allProductsExist(List<RequestBatchDTO> batches){
        for(RequestBatchDTO batch : batches)
            if (!productService.existsByIdProduct(batch.getIdProduct()))
                return batch.getIdProduct();
        return "";
    }

}
