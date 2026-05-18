package com.reservas.demo_reservas.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ReservaCreadaResponseDTO {

    private Long id;

    public ReservaCreadaResponseDTO(Long id) {
        this.id = id;
    }
}
