package com.eucaliptus.springboot_app_reports.controllers;

import com.eucaliptus.springboot_app_reports.dto.DatesDTO;
import com.eucaliptus.springboot_app_reports.dto.Message;
import com.eucaliptus.springboot_app_reports.service.ReportService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PostMapping("/dailyReport")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> dailyReport(@RequestBody DatesDTO dates, HttpServletRequest request) {
        try {
            return new ResponseEntity<>(reportService.getReports(
                    dates.getStartDate(), dates.getStartDate(), reportService.getTokenByRequest(request)),
                    HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new Message("Intente de nuevo mas tarde"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/rangeReport")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> rangeReport(@RequestBody DatesDTO dates, HttpServletRequest request) {
        try {
            return new ResponseEntity<>(reportService.getReports(
                    dates.getStartDate(), dates.getEndDate(), reportService.getTokenByRequest(request)),
                    HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new Message("Intente de nuevo mas tarde"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
