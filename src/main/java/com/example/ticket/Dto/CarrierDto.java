package com.example.ticket.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CarrierDto {

    int id;
    @Schema(description = "Название")
    String name;

    @Schema(description = "Номер телефона")
    int phoneNumber;

}
