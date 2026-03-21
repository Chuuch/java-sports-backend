package com.sports.platform.infrastructure.email;


import com.sports.platform.application.port.output.IEmailService;
import com.sports.platform.domain.event.UserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
@RequiredArgsConstructor
public class EventConsumer {
    private final IEmailService emailService;

    @Async("emailTaskExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleUserRegisteredEvent(UserRegisteredEvent event) {
        log.debug("Sending welcome email to {} following successful transaction commit.", event.email());
        emailService.sendWelcomeEmail(event.email(), event.firstName());
    }
}
