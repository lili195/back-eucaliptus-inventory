package com.eucaliptus.springboot_app_reports.controllers;

import com.eucaliptus.springboot_app_reports.dto.DatesDTO;
import com.eucaliptus.springboot_app_reports.dto.Message;
import com.eucaliptus.springboot_app_reports.model.ReportHistory;
import com.eucaliptus.springboot_app_reports.security.JwtTokenUtil;
import com.eucaliptus.springboot_app_reports.service.APIService;
import com.eucaliptus.springboot_app_reports.service.ReportHistoryService;
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
    @Autowired
    private ReportHistoryService reportHistoryService;
    @Autowired
    private APIService apiService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/dailyReport")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> dailyReport(@RequestBody DatesDTO dates, HttpServletRequest request) {
        try {
            reportHistoryService.save(new ReportHistory(
                    dates.getStartDate(), dates.getStartDate(),
                    jwtTokenUtil.extractUsername(apiService.getTokenByRequest(request))
            ));
            return new ResponseEntity<>(reportService.getReports(
                    dates.getStartDate(), dates.getStartDate(), apiService.getTokenByRequest(request)),
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
            reportHistoryService.save(new ReportHistory(
                    dates.getStartDate(), dates.getEndDate(),
                    jwtTokenUtil.extractUsername(apiService.getTokenByRequest(request))
            ));
            return new ResponseEntity<>(reportService.getReports(
                    dates.getStartDate(), dates.getEndDate(), apiService.getTokenByRequest(request)),
                    HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new Message("Intente de nuevo mas tarde"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
