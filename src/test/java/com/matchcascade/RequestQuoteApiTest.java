package com.matchcascade;

import com.matchcascade.customer.Customer;
import com.matchcascade.customer.CustomerRepository;
import com.matchcascade.partner.Partner;
import com.matchcascade.partner.PartnerRepository;
import com.matchcascade.quote.QuoteStatus;
import com.matchcascade.quote.dto.QuoteSubmitRequest;
import com.matchcascade.request.TimeSlot;
import com.matchcascade.request.dto.RequestCreateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
@AutoConfigureMockMvc
class RequestQuoteApiTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    PartnerRepository partnerRepository;

    @Test
    void 요청을_생성하고_견적을_제출한다() throws Exception {
        Customer customer = customerRepository.save(new Customer());
        Partner partner = partnerRepository.save(new Partner());

        RequestCreateRequest createRequest = new RequestCreateRequest(
                customer.getId(), LocalDate.now().plusDays(3), TimeSlot.AM, LocalDateTime.now().plusDays(2));

        String responseJson = mockMvc.perform(post("/api/requests")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerId").value(customer.getId()))
                .andExpect(jsonPath("$.status").value("OPEN"))
                .andReturn().getResponse().getContentAsString();

        Long requestId = objectMapper.readTree(responseJson).get("id").asLong();

        QuoteSubmitRequest submitRequest = new QuoteSubmitRequest(partner.getId(), new BigDecimal("150000"));

        mockMvc.perform(post("/api/requests/{requestId}/quotes", requestId)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(submitRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.requestId").value(requestId))
                .andExpect(jsonPath("$.partnerId").value(partner.getId()))
                .andExpect(jsonPath("$.priceSnapshot").value(150000))
                .andExpect(jsonPath("$.status").value(QuoteStatus.SUBMITTED.name()));
    }

    @Test
    void 존재하지_않는_고객으로_요청을_생성하면_404() throws Exception {
        RequestCreateRequest createRequest = new RequestCreateRequest(
                Long.MAX_VALUE, LocalDate.now().plusDays(3), TimeSlot.AM, LocalDateTime.now().plusDays(2));

        mockMvc.perform(post("/api/requests")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isNotFound());
    }

}
