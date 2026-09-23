package com.matchcascade.request.dto;

import com.matchcascade.request.TimeSlot;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record RequestCreateRequest(
        @NotNull Long customerId,
        @NotNull LocalDate desiredDate,
        @NotNull TimeSlot timeSlot,
        @NotNull LocalDateTime expiresAt
) {
}
