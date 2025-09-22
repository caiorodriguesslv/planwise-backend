package com.devilish.planwise.controllers.report;

import com.devilish.planwise.dto.report.FinancialSummaryResponse;
import com.devilish.planwise.services.report.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/financial-summary")
    public ResponseEntity<FinancialSummaryResponse> getFinancialSummary() {
        try {
            FinancialSummaryResponse summary = reportService.getFinancialSummary();
            return ResponseEntity.ok(summary);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/financial-summary/date-range")
    public ResponseEntity<FinancialSummaryResponse> getFinancialSummaryByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            FinancialSummaryResponse summary = reportService.getFinancialSummaryByDateRange(startDate, endDate);
            return ResponseEntity.ok(summary);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/goals-summary")
    public ResponseEntity<Map<String, Object>> getGoalsSummary() {
        try {
            Map<String, Object> summary = reportService.getGoalsSummary();
            return ResponseEntity.ok(summary);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/monthly-summary")
    public ResponseEntity<Map<String, Object>> getMonthlySummary(
            @RequestParam int year,
            @RequestParam int month) {
        try {
            Map<String, Object> summary = reportService.getMonthlySummary(year, month);
            return ResponseEntity.ok(summary);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/yearly-summary")
    public ResponseEntity<Map<String, Object>> getYearlySummary(@RequestParam int year) {
        try {
            Map<String, Object> summary = reportService.getYearlySummary(year);
            return ResponseEntity.ok(summary);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
