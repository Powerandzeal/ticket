package com.example.ticket.controllers;

import com.example.ticket.Dto.CarrierDto;
import com.example.ticket.services.CarrierService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Carrier controller", description = "Crud операции для перевозчика")
@RestController
@RequestMapping("/carrier")
@AllArgsConstructor
@Slf4j
public class CarririerController {
    CarrierService carrierService;


    /**
     * Создать перевозчика.
     *
     * @param carrierDto Данные для создания перевозчика.
     * @return ResponseEntity с результатом создания перевозчика.
     */
    @Operation(
            operationId = "createCarrier",
            summary = "Создать перевозчика",
            description = "Создает нового перевозчика на основе предоставленных данных.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "400", description = "Bad Request")
            }
    )
    @PostMapping("/create")
    public ResponseEntity<?> createCarrier(@RequestBody CarrierDto carrierDto) {
        if (carrierDto.getName() == null || carrierDto.getPhoneNumber() == 0) {
            return ResponseEntity.badRequest().body("Name, PhoneNumber cannot be null.");
        }

        // Если валидация прошла успешно, выполните создание билета
        return ResponseEntity.ok(carrierService.createCarrier(carrierDto));
    }

    /**
     * Обновить перевозчика.
     *
     * @param carrierId   Идентификатор перевозчика, который нужно обновить.
     * @param nameCarrier Новое имя перевозчика (опционально).
     * @param phoneNumber Новый номер телефона перевозчика (опционально).
     * @return ResponseEntity с результатом обновления перевозчика.
     */
    @Operation(
            operationId = "updateCarrier",
            summary = "Обновить перевозчика",
            description = "Обновляет существующего перевозчика на основе предоставленных данных.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "400", description = "Bad Request")
            }
    )
    @PutMapping("/updateCarrier")
    public ResponseEntity<?> updateCarrier(@RequestParam(required = false) Integer carrierId,
                                           @RequestParam(required = false) String nameCarrier,
                                           @RequestParam(required = false) Integer phoneNumber


    ) {
        if (carrierId <= 0 || phoneNumber <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(" carrierId или phoneNumber не могут быть отрицательными или равным 0 ");
        }
        return ResponseEntity.ok(carrierService.updateCarrier(carrierId, nameCarrier, phoneNumber));
    }

    /**
     * Удалить перевозчика по идентификатору.
     *
     * @param carrierId Идентификатор перевозчика для удаления.
     * @return ResponseEntity с результатом удаления перевозчика.
     */
    @Operation(
            operationId = "deleteCarrier",
            summary = "Удалить перевозчика",
            description = "Удаляет перевозчика по заданному идентификатору.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            }
    )
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
