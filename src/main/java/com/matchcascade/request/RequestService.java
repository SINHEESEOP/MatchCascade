package com.matchcascade.request;

import com.matchcascade.common.ResourceNotFoundException;
import com.matchcascade.customer.Customer;
import com.matchcascade.customer.CustomerRepository;
import com.matchcascade.request.dto.RequestCreateRequest;
import com.matchcascade.request.dto.RequestResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RequestService {

    private final RequestRepository requestRepository;
    private final CustomerRepository customerRepository;

    public RequestResponse create(RequestCreateRequest command) {
        Customer customer = customerRepository.findById(command.customerId())
                .orElseThrow(() -> new ResourceNotFoundException("고객을 찾을 수 없습니다: " + command.customerId()));

        Request request = new Request(customer, command.desiredDate(), command.timeSlot(), command.expiresAt());
        Request saved = requestRepository.save(request);

        return RequestResponse.from(saved);
    }

}
