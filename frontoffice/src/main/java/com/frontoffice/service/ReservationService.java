package com.frontoffice.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.frontoffice.dto.ReservationsResponse;
import com.frontoffice.model.Reservation;
import com.frontoffice.repository.ReservationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository; 

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${external.api.reservations.url:http://localhost:8080/voiture/api/reservations}")
    private String externalReservationsUrl;

    public List<Reservation> getAllReservations() {
        ResponseEntity<ReservationsResponse> response = restTemplate.getForEntity(
            externalReservationsUrl, ReservationsResponse.class);
        ReservationsResponse body = response.getBody();
        return body != null ? body.getReservations() : List.of();
    }

    public List<Reservation> getReservationsByDate(LocalDate date) {
        return reservationRepository.findByDateArriveeBetween(
                date.atStartOfDay(),
                date.plusDays(1).atStartOfDay()
        );
    }
}
