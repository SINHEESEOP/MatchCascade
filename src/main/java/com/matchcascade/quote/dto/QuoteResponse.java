package com.matchcascade.quote.dto;

import com.matchcascade.quote.Quote;
import com.matchcascade.quote.QuoteStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record QuoteResponse(
        Long id,
        Long requestId,
        Long partnerId,
        BigDecimal priceSnapshot,
        QuoteStatus status,
        LocalDateTime submittedAt
) {

    public static QuoteResponse from(Quote quote) {
        return new QuoteResponse(
                quote.getId(),
                quote.getRequest().getId(),
                quote.getPartner().getId(),
                quote.getPriceSnapshot(),
                quote.getStatus(),
                quote.getSubmittedAt()
        );
    }

}
