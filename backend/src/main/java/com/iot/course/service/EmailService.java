package com.iot.course.service;

import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.iot.course.dto.report.ReportDTO;
import com.iot.course.dto.report.PopularMovieDTO;
import com.iot.course.dto.report.PopularGenreDTO;
import com.iot.course.dto.report.UserActivityDTO;
import com.iot.course.dto.report.SubscriptionRevenueDTO;

@Service
public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Value("${report.email.recipient:}")
    private String recipientEmail;

    @Value("${spring.mail.username:}")
    private String mailUsername;

    public void sendReport(ReportDTO report) {
        if (recipientEmail == null || recipientEmail.isEmpty()) {
            logger.debug("Recipient email is not configured, skipping report send");
            return;
        }

        if (mailSender == null) {
            logger.warn("JavaMailSender is not configured, cannot send report");
            return;
        }

        if (mailUsername == null || mailUsername.isEmpty()) {
            logger.warn("Email username is not configured, cannot send report");
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(recipientEmail);
            message.setSubject("Online Cinema Report");
            message.setText(formatReport(report));

            mailSender.send(message);
            logger.info("Report sent successfully to {}", recipientEmail);
        } catch (org.springframework.mail.MailAuthenticationException e) {
            logger.warn("Email authentication failed. Please check email credentials. Error: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Failed to send report email: {}", e.getMessage(), e);
        }
    }

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    private String formatReport(ReportDTO report) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("ONLINE CINEMA REPORT\n");
        sb.append("================================\n\n");
        
        sb.append("MOST POPULAR MOVIES:\n");
        sb.append("----------------------\n");
        if (report.popularMovies().isEmpty()) {
            sb.append("No data\n");
        } else {
            int index = 1;
            for (PopularMovieDTO movie : report.popularMovies()) {
                sb.append(String.format("%d. %s - %d views\n", 
                    index++, movie.title(), movie.viewCount()));
            }
        }
        sb.append("\n");
        
        sb.append("MOST POPULAR GENRES:\n");
        sb.append("----------------------\n");
        if (report.popularGenres().isEmpty()) {
            sb.append("No data\n");
        } else {
            int index = 1;
            for (PopularGenreDTO genre : report.popularGenres()) {
                sb.append(String.format("%d. %s - %d views\n", 
                    index++, genre.genreName(), genre.viewCount()));
            }
        }
        sb.append("\n");
        
        UserActivityDTO activity = report.userActivity();
        sb.append("USER ACTIVITY:\n");
        sb.append("-------------------------\n");
        sb.append(String.format("Total users: %d\n", activity.totalUsers()));
        sb.append(String.format("Active users (last 30 days): %d\n", activity.activeUsers()));
        sb.append(String.format("Total views (last 30 days): %d\n", activity.totalViews()));
        sb.append(String.format("Report date: %s\n", activity.reportDate().format(DATE_FORMATTER)));
        sb.append("\n");
        
        SubscriptionRevenueDTO revenue = report.subscriptionRevenue();
        sb.append("SUBSCRIPTION REVENUE:\n");
        sb.append("---------------------\n");
        sb.append(String.format("Total subscriptions: %d\n", revenue.totalSubscriptions()));
        sb.append(String.format("Total revenue: %.2f\n", revenue.totalRevenue()));
        sb.append(String.format("Average revenue per subscription: %.2f\n", revenue.averageRevenue()));
        
        return sb.toString();
    }
}

