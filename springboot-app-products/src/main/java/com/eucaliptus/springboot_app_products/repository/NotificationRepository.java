package com.eucaliptus.springboot_app_products.repository;

import com.eucaliptus.springboot_app_products.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    List<Notification> findTop15ByOrderByDateDesc();
}
