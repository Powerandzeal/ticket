package com.example.ticket.controllers;

import com.example.ticket.Dto.CarrierDto;
import com.example.ticket.models.Carrier;
import com.example.ticket.services.CarrierService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Carrier controller", description = "Crud operation for carrier")
@RestController
@RequestMapping("/carrier")
@AllArgsConstructor
@Slf4j
public class CarririerController {
    CarrierService carrierService;

    @GetMapping()
    public Carrier getCurrier() {
        return null;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createCarrier(@RequestBody CarrierDto routeDTO) {
        if (routeDTO.getName() == null || routeDTO.getPhoneNumber() == 0) {
            return ResponseEntity.badRequest().body("Name, PhoneNumber cannot be null.");
        }

        // Если валидация прошла успешно, выполните создание билета
        return ResponseEntity.ok(carrierService.createCarrier(routeDTO));
    }

    @PutMapping("/updateCarrier")
    public ResponseEntity<?> updateRoute(@RequestParam(required = false) Integer carrierId,
                                         @RequestParam(required = false) String nameCarrier,
                                         @RequestParam(required = false) Integer phoneNumber


    ) {
        // Если валидация прошла успешно, выполните создание билета
        return ResponseEntity.ok(carrierService.updateCarrier(carrierId,nameCarrier, phoneNumber));
    }

    @DeleteMapping("/deleteCarrier")
    public ResponseEntity<String> deleteTicket(@RequestParam(value = "routeId") int carrierId) {
        boolean isDeleted = carrierService.deleteCarrierById(carrierId);

        if (isDeleted) {
            return ResponseEntity.ok("маршрут успешно удален");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("маршрут с указанным идентификатором не найден");
        }
    }
}
