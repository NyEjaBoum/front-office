package com.frontoffice.controller;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.frontoffice.model.Reservation;
import com.frontoffice.service.ReservationService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping("/reservations")
    public String listeReservations(
            @RequestParam(name = "date", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Model model) {

        List<Reservation> reservations;
        if (date != null) {
            reservations = reservationService.getReservationsByDate(date);
        } else {
            reservations = reservationService.getAllReservations();
        }

        // Conversion LocalDateTime -> java.util.Date pour chaque réservation
        List<Map<String, Object>> reservationsForView = reservations.stream().map(res -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", res.getId());
            map.put("idClient", res.getIdClient());
            map.put("hotel", res.getHotel());
            map.put("dateArrivee", Date.from(res.getDateArrivee().atZone(ZoneId.systemDefault()).toInstant()));
            return map;
        }).collect(Collectors.toList());

        model.addAttribute("reservations", reservationsForView);
        model.addAttribute("dateFiltre", date);
        return "reservations";
    }
}
