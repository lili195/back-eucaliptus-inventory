package com.eucaliptus.springboot_app_notifications.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // SERIAL equivale a auto-generado
    @Column(name = "id_notificacion", nullable = false)
    private Long idNotification;

    @Column(name = "id_stock", nullable = false)
    private Integer idStock;

    @Column(name = "id_product", nullable = false, length = 255)
    private String idProduct;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime notificationDate; // TIMESTAMP mapeado a LocalDateTime

    @Column(name = "mensaje", nullable = false, length = 255)
    private String message;

    // Getters y Setters
    public Long getIdNotification() {
        return idNotification;
    }

    public void setIdNotification(Long idNotification) {
        this.idNotification = idNotification;
    }

    public Integer getIdStock() {
        return idStock;
    }

    public void setIdStock(Integer idStock) {
        this.idStock = idStock;
    }

    public String getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(String idProduct) {
        this.idProduct = idProduct;
    }

    public LocalDateTime getNotificationDate() {
        return notificationDate;
    }

    public void setNotificationDate(LocalDateTime notificationDate) {
        this.notificationDate = notificationDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}