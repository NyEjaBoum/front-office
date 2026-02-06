package com.frontoffice.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.frontoffice.model.Reservation;
import com.frontoffice.repository.ReservationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final RestClient restClient;

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${external.api.reservations.url:http://localhost:8080/api/reservations}")
    private String externalReservationsUrl;

    public List<Reservation> getAllReservations() {
        // Appel à l'API REST externe
        ResponseEntity<Reservation[]> response = restTemplate.getForEntity(externalReservationsUrl, Reservation[].class);
        Reservation[] reservations = response.getBody();
        return reservations != null ? List.of(reservations) : List.of();
    }

    public List<ReservationDTO> getReservationsByDate(LocalDate date) {
        return getAllReservations().stream()
                .filter(r -> r.getDateArrivee() != null
                        && r.getDateArrivee().toLocalDate().equals(date))
                .toList();
    }
}
