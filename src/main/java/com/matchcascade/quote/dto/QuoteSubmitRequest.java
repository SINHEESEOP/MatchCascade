package com.matchcascade.quote.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record QuoteSubmitRequest(
        @NotNull Long partnerId,
        @NotNull @Positive BigDecimal price
) {
}
