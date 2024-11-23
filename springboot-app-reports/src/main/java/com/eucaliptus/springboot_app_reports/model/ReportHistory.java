package com.eucaliptus.springboot_app_reports.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "report_history")
public class ReportHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_report")
    private Long idReport;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "start_date", nullable = false, columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "finish_date", nullable = false, columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private Date finishDate;

    @Column(name = "account_solicited")
    private String accountSolicited;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "create_date", nullable = false, columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private Date createDate;

    public ReportHistory(Date startDate, Date finishDate, String accountSolicited) {
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.accountSolicited = accountSolicited;
        this.createDate = new Date();
    }

}
