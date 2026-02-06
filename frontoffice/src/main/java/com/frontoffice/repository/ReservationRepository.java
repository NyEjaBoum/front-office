package com.frontoffice.repository;

import java.time.LocalDateTime;
import java.util.List;

import com.frontoffice.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    List<Reservation> findByDateArriveeBetween(LocalDateTime debut, LocalDateTime fin);
}
