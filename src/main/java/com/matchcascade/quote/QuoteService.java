package com.matchcascade.quote;

import com.matchcascade.common.ResourceNotFoundException;
import com.matchcascade.partner.Partner;
import com.matchcascade.partner.PartnerRepository;
import com.matchcascade.quote.dto.QuoteResponse;
import com.matchcascade.quote.dto.QuoteSubmitRequest;
import com.matchcascade.request.Request;
import com.matchcascade.request.RequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class QuoteService {

    private final QuoteRepository quoteRepository;
    private final RequestRepository requestRepository;
    private final PartnerRepository partnerRepository;

    public QuoteResponse submit(Long requestId, QuoteSubmitRequest command) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("요청을 찾을 수 없습니다: " + requestId));
        Partner partner = partnerRepository.findById(command.partnerId())
                .orElseThrow(() -> new ResourceNotFoundException("파트너를 찾을 수 없습니다: " + command.partnerId()));

        Quote quote = new Quote(request, partner, command.price());
        Quote saved = quoteRepository.save(quote);

        return QuoteResponse.from(saved);
    }

}
