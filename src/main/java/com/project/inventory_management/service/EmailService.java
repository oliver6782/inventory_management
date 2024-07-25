package com.project.inventory_management.service;

import com.project.inventory_management.entity.Medication;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class EmailService {
    private final JavaMailSender javaMailSender;
    private static final String EMAIL_FROM = "";
//    private static final String EMAIL_TO = "";

    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }
/*
** This method use simple email message which is plain text format
    @Async
    public void sendEmail(String to, List<Medication> insufficientMedications) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(EMAIL_FROM);
        message.setTo(to);
        message.setSubject("Insufficient Medications");
        StringBuilder emailContent = new StringBuilder();
        emailContent.append("Medications are insufficient for the following products:\n\n");

        for (Medication medication : insufficientMedications) {
            emailContent.append("Medication ID: ").append(medication.getId()).append("\n")
                    .append("Name: ").append(medication.getName()).append("\n")
                    .append("Quantity: ").append(medication.getQuantity()).append("\n\n");
        }
        message.setText(emailContent.toString());

        try {
            javaMailSender.send(message);
            System.out.println("Sent message successfully...");
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
            e.printStackTrace();
        }

    }
 */
    // This method enables html format emails
    @Async
    public void sendEmail(String to, List<Medication> insufficientMedications) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper;

        try {
            helper = new MimeMessageHelper(message, true);
            helper.setFrom(EMAIL_FROM);
            helper.setTo(to);
            helper.setSubject("Insufficient Medications");

            StringBuilder emailContent = new StringBuilder();
            emailContent.append("<h3>Medications are insufficient for the following products:</h3>");
            emailContent.append("<table border='1' style='border-collapse: collapse;'>")
                    .append("<tr>")
                    .append("<th>Medication ID</th>")
                    .append("<th>Name</th>")
                    .append("<th>Quantity</th>")
                    .append("</tr>");

            for (Medication medication : insufficientMedications) {
                emailContent.append("<tr>")
                        .append("<td>").append(medication.getId()).append("</td>")
                        .append("<td>").append(medication.getName()).append("</td>")
                        .append("<td>").append(medication.getQuantity()).append("</td>")
                        .append("</tr>");
            }

            emailContent.append("</table>");

            helper.setText(emailContent.toString(), true); // Set to true to send HTML email
            javaMailSender.send(message);
            System.out.println("Sent message successfully...");
        } catch (MessagingException e) {
            System.err.println("Failed to send email: " + e.getMessage());
            e.printStackTrace();
        }

    }

}
