package com.sports.platform.infrastructure.email;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import com.sports.platform.application.port.output.IEmailService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceAdapter implements IEmailService {
    private final Resend resend;

    @Value("${app.email.from}")
    private String fromEmail;

    private final Counter emailSuccessCounter;
    private final Counter emailFailureCounter;

    public EmailServiceAdapter(
            Resend resend,
            MeterRegistry registry,
            @Value("${app.email.from}") String fromEmail
    ) {
        this.resend = resend;
        this.fromEmail = fromEmail;
        this.emailSuccessCounter = Counter.builder("email.sent.total")
                .tag("status", "success")
                .description("Total number of successfully sent emails")
                .register(registry);
        this.emailFailureCounter = Counter.builder("email.sent.total")
                .tag("status", "failure")
                .description("Total number of failed email attempts")
                .register(registry);
    }

    @Override
    public void sendWelcomeEmail(String to, String firstName) {
        CreateEmailOptions params = CreateEmailOptions.builder()
                .from(fromEmail)
                .to(to)
                .subject("Welcome to the Arena!")
                .html("<strong>Hi " + firstName + ",</strong><p>Thanks for joining the sports platform!</p>")
                .build();

        try {
            CreateEmailResponse response = resend.emails().send(params);
            log.info("Email sent successfully via Resend. ID: {}", response.getId());
            emailSuccessCounter.increment();
        } catch (ResendException e) {
            log.error("Resend SDK error while sending to {}: {}", to, e.getMessage());
            emailFailureCounter.increment();
            // Add retry queue later
        }
    }
}
