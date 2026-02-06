package com.frontoffice.service;

import java.time.LocalDate;
import java.util.List;

import com.frontoffice.model.Reservation;
import com.frontoffice.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public List<Reservation> getReservationsByDate(LocalDate date) {
        return reservationRepository.findByDateArriveeBetween(
                date.atStartOfDay(),
                date.plusDays(1).atStartOfDay()
        );
    }
}
