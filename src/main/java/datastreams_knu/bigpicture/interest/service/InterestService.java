package datastreams_knu.bigpicture.interest.service;

import datastreams_knu.bigpicture.interest.controller.dto.InterestResponse;
import datastreams_knu.bigpicture.interest.repository.KoreaInterestRepository;
import datastreams_knu.bigpicture.interest.repository.USInterestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class InterestService {

    private final KoreaInterestRepository koreaInterestRepository;
    private final USInterestRepository usInterestRepository;

    public List<InterestResponse> getKoreaInterests() {
        return koreaInterestRepository.findAll().stream()
            .map(interest -> InterestResponse.from(interest))
            .collect(Collectors.toList());
    }

    public List<InterestResponse> getUSInterests() {
        return usInterestRepository.findAll().stream()
            .map(interest -> InterestResponse.from(interest))
            .collect(Collectors.toList());
    }
}
