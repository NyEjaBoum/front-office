package com.frontoffice.dao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.frontoffice.model.Reservation;

@Component
public class ReservationDAO {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${backoffice.api.url:http://localhost:8080/voiture/api}/reservations")
    private String externalReservationsUrl;

    @Value("${api.token:}")
    private String apiToken;

    // Toutes les réservations via l'API REST
    public List<Reservation> getAllReservations() {
        System.out.println("[ReservationDAO] Appel de l'API : " + externalReservationsUrl);

        HttpHeaders headers = new HttpHeaders();
        if (apiToken != null && !apiToken.isEmpty()) {
            headers.set("X-API-TOKEN", apiToken);
        }
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<ReservationsResponse> responseEntity;
        try {
            responseEntity = restTemplate.exchange(
                externalReservationsUrl, HttpMethod.GET, request, ReservationsResponse.class);
        } catch (RestClientException e) {
            throw new RuntimeException("Back Office indisponible : " + e.getMessage());
        }

        ReservationsResponse response = responseEntity.getBody();

        if (response == null) {
            throw new RuntimeException("Reponse vide du Back Office");
        }
        if (response.getError() != null && !response.getError().isEmpty()) {
            throw new RuntimeException(response.getError());
        }
        if (response.getData() == null) {
            return List.of();
        }

        System.out.println("[ReservationDAO] Nombre de réservations reçues : " + response.getData().size());
        return response.getData();
    }

    // Filtrer par date côté DAO (simple exemple)
    public List<Reservation> getReservationsByDate(LocalDate date) {
        List<Reservation> all = getAllReservations();

        System.out.println("[ReservationDAO] Filtrage par date : " + date);
        if (date == null) {
            System.out.println("[ReservationDAO] Pas de filtre, retour de toutes les réservations.");
            return all;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        List<Reservation> filtered = all.stream()
                .filter(r -> r.getDateArrivee() != null)
                .filter(r -> {
                    try {
                        LocalDateTime ldt = LocalDateTime.parse(r.getDateArrivee(), formatter);
                        boolean match = ldt.toLocalDate().isEqual(date);
                        if (match) {
                            System.out.println("[ReservationDAO] Réservation matchée : id=" + r.getId() + ", dateArrivee=" + r.getDateArrivee());
                        }
                        return match;
                    } catch (Exception e) {
                        System.out.println("[ReservationDAO] Erreur parsing dateArrivee='" + r.getDateArrivee() + "' pour la réservation id=" + r.getId());
                        return false;
                    }
                })
                .toList();

        System.out.println("[ReservationDAO] Nombre de réservations après filtrage : " + filtered.size());
        return filtered;
    }

    // DTO interne — mappe le JSON du Back Office (champ "data" ou "error")
    public static class ReservationsResponse {
        private List<Reservation> data;
        private String error;

        public List<Reservation> getData() { return data; }
        public void setData(List<Reservation> data) { this.data = data; }

        public String getError() { return error; }
        public void setError(String error) { this.error = error; }
    }
}