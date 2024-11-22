package com.eucaliptus.springboot_app_billing.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

import java.math.BigDecimal;

@Entity
@Table(name = "client_summary")
public class Summary {
    @Id
    private Long id = 1L;

    @Column(name = "total_sales_income")
    private BigDecimal totalSalesIncome;

    @Column(name = "total_purchase_investment")
    private BigDecimal totalPurchaseInvestment;

    @Column(name = "total_sales_count")
    private int totalSalesCount;

    @Column(name = "total_purchases_count")
    private int totalPurchasesCount;

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getTotalSalesIncome() {
        return totalSalesIncome;
    }

    public void setTotalSalesIncome(BigDecimal totalSalesIncome) {
        this.totalSalesIncome = totalSalesIncome;
    }

    public BigDecimal getTotalPurchaseInvestment() {
        return totalPurchaseInvestment;
    }

    public void setTotalPurchaseInvestment(BigDecimal totalPurchaseInvestment) {
        this.totalPurchaseInvestment = totalPurchaseInvestment;
    }

    public int getTotalSalesCount() {
        return totalSalesCount;
    }

    public void setTotalSalesCount(int totalSalesCount) {
        this.totalSalesCount = totalSalesCount;
    }

    public int getTotalPurchasesCount() {
        return totalPurchasesCount;
    }

    public void setTotalPurchasesCount(int totalPurchasesCount) {
        this.totalPurchasesCount = totalPurchasesCount;
    }
}
