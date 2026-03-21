package com.sports.platform.application.port.output;

public interface IEmailService {
    void sendWelcomeEmail(String to, String firstName);
}
