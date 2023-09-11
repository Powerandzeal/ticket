package com.example.ticket.controllers;

import com.example.ticket.Dto.RouteDTO;
import com.example.ticket.models.Route;
import com.example.ticket.services.RouteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Tag(name = "Route controller", description = "Crud операции для перевозчика")
@RestController
@RequestMapping("/route")
@AllArgsConstructor
@Slf4j
public class RouteController {
    private final RouteService routeService;

    /**
     * Создать маршрут.
     *
     * @param routeDTO Данные для создания маршрута.
     * @return ResponseEntity с результатом создания маршрута.
     */
    @Operation(
            operationId = "createRoute",
            summary = "Создать маршрут",
            description = "Создает новый маршрут на основе предоставленных данных.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "400", description = "Bad Request")
            }
    )
    @PostMapping("/route")
    public ResponseEntity<?> createRoute(@Valid @RequestBody RouteDTO routeDTO) {
        if (routeDTO.getCarrierId() == 0 || routeDTO.getPointOfDeparture() == null || routeDTO.getDestination() == null ||
                routeDTO.getTripDuration() == 0) {
            return ResponseEntity.badRequest().body("CarrierId, Departure, Destination, and TripDuration не могут быть отрицательными или равным 0.");
        }

        return ResponseEntity.ok(routeService.createRoute(routeDTO));
    }

    /**
     * Обновить маршрут.
     *
     * @param routeId      Идентификатор маршрута, который нужно обновить.
     * @param departure    Новая точка отправления для маршрута (опционально).
     * @param destination  Новое место назначения для маршрута (опционально).
     * @param carrierId    Новый идентификатор перевозчика для маршрута (опционально).
     * @param tripDuration Новая продолжительность путешествия для маршрута (опционально).
     * @return ResponseEntity с результатом обновления маршрута.
     */
    @Operation(
            operationId = "updateRoute",
            summary = "Обновить маршрут",
            description = "Обновляет существующий маршрут на основе предоставленных данных.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "400", description = "Bad Request")
            }
    )
    @PutMapping("/updateRoute")
    public ResponseEntity<?> updateRoute(
            @RequestParam @NotNull Integer routeId,
            @RequestParam(required = false) String departure,
            @RequestParam(required = false) String destination,
            @RequestParam(required = false) Integer carrierId,
            @RequestParam(required = false) Integer tripDuration
    ) {
        if (routeId <= 0 || carrierId <= 0 || tripDuration < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Значение routeId , carrierId, tripDuration не могут быть отрицательными ");
        }
        // Валидация прошла успешно
        return ResponseEntity.ok(routeService.updateRoute(routeId, departure, destination, carrierId, tripDuration));
    }

    /**
     * Удалить маршрут по идентификатору.
     *
     * @param routeId Идентификатор маршрута для удаления.
     * @return ResponseEntity с результатом удаления маршрута.
     */
    @Operation(
            operationId = "deleteRoute",
            summary = "Удалить маршрут",
            description = "Удаляет маршрут по заданному идентификатору.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            }
    )
    @DeleteMapping("/deleteRoute")
    public ResponseEntity<String> deleteTicket(@RequestParam(value = "routeId") int routeId) {
        boolean isDeleted = routeService.deleteRouteById(routeId);

        if (isDeleted) {
            return ResponseEntity.ok("маршрут успешно удален");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("маршрут с указанным идентификатором не найден");
        }
    }
}
