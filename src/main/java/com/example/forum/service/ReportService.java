package com.example.forum.service;

import com.example.forum.controller.form.ReportForm;
import com.example.forum.repository.ReportRepository;
import com.example.forum.repository.entity.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReportService {
    @Autowired
    ReportRepository reportRepository;

    /*
     * レコード全件取得処理
     */
    public List<ReportForm> findAllReport() {
        List<Report> results = reportRepository.findAllByOrderByIdDesc();
        List<ReportForm> reports = setReportForm(results);
        return reports;
    }

    /*
     * レコード取得(日付絞り込み)処理
     */
    public List<ReportForm> findReportBetween(LocalDate sinceDate, LocalDate untilDate) {

        sinceDate = sinceDate == null ? LocalDate.of(2020,1,1) : sinceDate;
        untilDate = untilDate == null ? LocalDate.now() : untilDate;

        LocalDateTime sinceDateTime = sinceDate.atStartOfDay();
        LocalDateTime untilDateTime = untilDate.atTime(23, 59, 59);

        List<Report> results = reportRepository.findByCreatedDateBetweenOrderByThreadUpdatedDateDesc(sinceDateTime, untilDateTime);
        return setReportForm(results);
    }

    /*
     * レコード追加
     */
    public void saveReport(ReportForm reqReport) {
        Report saveReport = setReportEntity(reqReport);
        LocalDateTime current = LocalDateTime.now();
        saveReport.setUpdatedDate(current);
        saveReport.setThreadUpdatedDate(current);
        reportRepository.save(saveReport);
    }

    public void deleteReport(Integer id) {
        reportRepository.deleteById(id);
    }

    /*
     * レコード1件取得
     */
    public ReportForm findReport(Integer id) {
        List<Report> results = new ArrayList<>();
        results.add(reportRepository.findById(id).orElse(null));
        List<ReportForm> reports = setReportForm(results);
        return reports.get(0);
    }

    /*
     * DBから取得したデータをFormに設定
     */
    private List<ReportForm> setReportForm(List<Report> results) {
        List<ReportForm> reports = new ArrayList<>();

        for (int i = 0; i < results.size(); i++) {
            ReportForm report = new ReportForm();
            Report result = results.get(i);
            report.setId(result.getId());
            report.setContent(result.getContent());
            report.setCreatedDate(result.getCreatedDate());
            report.setUpdatedDate(result.getUpdatedDate());
            report.setThreadUpdatedDate(result.getThreadUpdatedDate());
            reports.add(report);
        }
        return reports;
    }

    /*
     * リクエストから取得した情報をEntityに設定
     */
    private Report setReportEntity(ReportForm reqReport) {
        Report report = new Report();
        report.setId(reqReport.getId());
        report.setContent(reqReport.getContent());
        report.setCreatedDate(reqReport.getCreatedDate());
        report.setUpdatedDate(reqReport.getUpdatedDate());
        report.setThreadUpdatedDate(reqReport.getThreadUpdatedDate());
        return report;
    }
}

