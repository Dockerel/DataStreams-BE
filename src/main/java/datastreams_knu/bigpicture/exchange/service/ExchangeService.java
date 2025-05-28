package datastreams_knu.bigpicture.exchange.service;

import datastreams_knu.bigpicture.exchange.controller.dto.ExchangeResponse;
import datastreams_knu.bigpicture.exchange.repository.ExchangeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ExchangeService {

    public final ExchangeRepository exchangeRepository;

    public List<ExchangeResponse> getExchanges() {
        return exchangeRepository.findAll().stream()
                .map(exchange -> ExchangeResponse.from(exchange))
                .collect(Collectors.toList());
    }
}
