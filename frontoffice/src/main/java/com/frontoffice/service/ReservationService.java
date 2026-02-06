package com.frontoffice.service;

import java.time.LocalDate;
import java.util.List;

import com.frontoffice.dto.ReservationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final RestClient restClient;

    public List<ReservationDTO> getAllReservations() {
        return restClient.get()
                .uri("/api/reservations")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    public List<ReservationDTO> getReservationsByDate(LocalDate date) {
        return getAllReservations().stream()
                .filter(r -> r.getDateArrivee() != null
                        && r.getDateArrivee().toLocalDate().equals(date))
                .toList();
    }
}
