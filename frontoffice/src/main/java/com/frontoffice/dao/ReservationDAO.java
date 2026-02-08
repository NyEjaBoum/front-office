package com.frontoffice.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.frontoffice.model.Reservation;

@Component
public class ReservationDAO {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${backoffice.api.url:http://localhost:8080/voiture/api}/reservations")
    private String externalReservationsUrl;

    // Toutes les réservations via l'API REST
    public List<Reservation> getAllReservations() {
        ReservationsResponse response = restTemplate.getForObject(
            externalReservationsUrl, ReservationsResponse.class);
        
        if (response == null || response.getReservations() == null) {
            return List.of();
        }
    
        return response.getReservations();
    }

    // Filtrer par date côté DAO (simple exemple)
    public List<Reservation> getReservationsByDate(LocalDate date) {
        List<Reservation> all = getAllReservations();
        
        if (date == null) {
            return all; // pas de filtre → tout
        }
    
        return all.stream()
                .filter(r -> r.getDateArrivee() != null)
                .filter(r -> r.getDateArrivee().toLocalDate().isEqual(date))
                .toList();
    }

    // DTO interne
    public static class ReservationsResponse {
        private List<Reservation> reservations;
        public List<Reservation> getReservations() { return reservations; }
        public void setReservations(List<Reservation> reservations) { this.reservations = reservations; }
    }
}
