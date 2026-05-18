package com.reservas.demo_reservas.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class GananciasResponseDTO {

    Long salonId;

    String periodo;

    BigDecimal total;

}
