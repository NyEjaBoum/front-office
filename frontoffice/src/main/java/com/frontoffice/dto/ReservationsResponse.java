package com.frontoffice.dto;

import com.frontoffice.model.Reservation;
import java.util.List;

public class ReservationsResponse {
    private List<Reservation> reservations;

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }
}