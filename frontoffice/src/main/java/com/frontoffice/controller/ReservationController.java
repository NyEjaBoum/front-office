package com.frontoffice.controller;

import java.time.LocalDate;
import java.util.List;

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

        model.addAttribute("reservations", reservations);
        model.addAttribute("dateFiltre", date);
        return "reservations";
    }
}
