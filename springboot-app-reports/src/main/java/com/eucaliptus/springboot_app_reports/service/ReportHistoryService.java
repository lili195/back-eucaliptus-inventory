package com.eucaliptus.springboot_app_reports.service;

import com.eucaliptus.springboot_app_reports.model.ReportHistory;
import com.eucaliptus.springboot_app_reports.repository.ReportHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportHistoryService {

    @Autowired
    private ReportHistoryRepository reportHistoryRepository;

    public ReportHistory save(ReportHistory reportHistory) {
        return reportHistoryRepository.save(reportHistory);
    }
}
