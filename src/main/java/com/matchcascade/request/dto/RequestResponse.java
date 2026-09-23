package com.matchcascade.request.dto;

import com.matchcascade.request.Request;
import com.matchcascade.request.RequestStatus;
import com.matchcascade.request.TimeSlot;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record RequestResponse(
        Long id,
        Long customerId,
        LocalDate desiredDate,
        TimeSlot timeSlot,
        RequestStatus status,
        LocalDateTime expiresAt
) {

    public static RequestResponse from(Request request) {
        return new RequestResponse(
                request.getId(),
                request.getCustomer().getId(),
                request.getDesiredDate(),
                request.getTimeSlot(),
                request.getStatus(),
                request.getExpiresAt()
        );
    }

}
