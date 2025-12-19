package com.iot.course.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ReportScheduler {
    private static final Logger logger = LoggerFactory.getLogger(ReportScheduler.class);

    @Autowired
    private ReportService reportService;

    @Autowired
    private EmailService emailService;

    @Value("${report.email.recipient:}")
    private String recipientEmail;

    @Scheduled(fixedRate = 300000)
    public void sendScheduledReport() {
        if (recipientEmail == null || recipientEmail.isEmpty()) {
            logger.debug("Report email recipient not configured, skipping scheduled report");
            return;
        }

        try {
            logger.info("Generating and sending scheduled report");
            var report = reportService.generateReport();
            emailService.sendReport(report);
        } catch (Exception e) {
            logger.warn("Failed to send scheduled report, will retry in next cycle: {}", e.getMessage());
        }
    }
}

