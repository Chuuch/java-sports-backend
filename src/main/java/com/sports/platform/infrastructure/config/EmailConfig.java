package com.sports.platform.infrastructure.config;

import com.resend.Resend;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailConfig {
    @Value("${app.email.resend.api-key}")
    private String apiKey;

    @Bean
    public Resend resendClient() {
        return new Resend(apiKey);
    }
}
