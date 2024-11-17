package com.eucaliptus.springboot_app_products.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReduceStockService {

    @Autowired
    private StockService stockService;
    @Autowired
    private BatchService batchService;
}
