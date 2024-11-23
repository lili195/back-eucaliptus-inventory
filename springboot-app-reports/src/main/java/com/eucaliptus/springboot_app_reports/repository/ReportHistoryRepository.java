package com.eucaliptus.springboot_app_reports.repository;

import com.eucaliptus.springboot_app_reports.model.ReportHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportHistoryRepository extends JpaRepository<ReportHistory, Long> {
}
