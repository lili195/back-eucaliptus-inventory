package com.eucaliptus.springboot_app_notifications.repository;

import com.eucaliptus.springboot_app_notifications.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

}
