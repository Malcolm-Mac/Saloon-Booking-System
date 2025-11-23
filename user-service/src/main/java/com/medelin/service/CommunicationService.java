package com.medelin.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;

@Slf4j
@Service
@AllArgsConstructor
public class CommunicationService implements ICommunicationService
{
    private final JavaMailSender mailSender;

    public void sendPasswordResetEmail(String to, String link)
    {
        try
        {
            SimpleMailMessage message = getSimpleMailMessage(to, link);
            mailSender.send(message);
        } catch (MailAuthenticationException ex) {
            log.error("Email authentication failed: {}", ex.getMessage(), ex);
        } catch (MailSendException ex) {
            log.error("Email sending failed: {}", ex.getMessage(), ex);
        } catch (Exception ex) {
            log.error("Unexpected error while sending email: {}", ex.getMessage(), ex);
        }
    }

    private static SimpleMailMessage getSimpleMailMessage(String to, String link)
    {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("info@asynctechnologies.co.za");
        message.setTo(to);
        message.setSubject("Reset Your Password");
        message.setText("""
            You requested to reset your password.

            Click the link below to reset it:
            """ + link + """

            This link will expire in 10 minutes.
            If you did not request this, ignore this email.
            """);
        return message;
    }
}
