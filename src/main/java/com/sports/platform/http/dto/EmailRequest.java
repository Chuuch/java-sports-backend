package com.sports.platform.http.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record EmailRequest(
        @JsonProperty("from") String from,
        @JsonProperty("to") String to,
        @JsonProperty("subject") String subject,
        @JsonProperty("html") String html
) {}
