package com.frontoffice.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.frontoffice.dao.ReservationDAO;
import com.frontoffice.model.Reservation;

@Controller
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationDAO reservationDAO;

    public ReservationController(ReservationDAO reservationDAO) {
        this.reservationDAO = reservationDAO;
    }

    @GetMapping
    public String list(
        @RequestParam(name = "date", required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate date,
        Model model
    ) {
        try {
            List<Reservation> reservations =
                (date == null)
                ? reservationDAO.getAllReservations()
                : reservationDAO.getReservationsByDate(date);

            model.addAttribute("reservations", reservations);
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "reservations";
    }
}

