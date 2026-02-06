package com.frontoffice.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReservationDTO {
    private Integer id;
    private Integer idClient;
    private HotelDTO hotel;
    private LocalDateTime dateArrivee;
}
