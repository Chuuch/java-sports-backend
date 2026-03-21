package com.sports.platform.infrastructure.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "app")
@Validated
public record AppProperties(@NotNull Stripe stripe, @NotNull Mail mail) {
    public record Stripe(
            @NotBlank String apiKey
    ){}
    public record Mail(
            @NotBlank String host,
            @NotNull Integer port,
            @NotBlank String username,
            @NotBlank String password
    ){}
}
