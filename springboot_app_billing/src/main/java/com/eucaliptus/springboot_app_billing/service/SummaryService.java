package com.eucaliptus.springboot_app_billing.service;

import com.eucaliptus.springboot_app_billing.model.Summary;
import com.eucaliptus.springboot_app_billing.repository.SummaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SummaryService {

    @Autowired
    private SummaryRepository clientSummaryRepository;

    public Optional<Summary> getGlobalSummary() {
        return clientSummaryRepository.findGlobalSummary();
    }
}
