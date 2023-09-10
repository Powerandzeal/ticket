package com.example.ticket.controllers;

import com.example.ticket.Dto.RouteDTO;
import com.example.ticket.models.Route;
import com.example.ticket.services.RouteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
@Tag(name = "Route controller", description = "Crud operation for route")
@RestController
@RequestMapping("/route")
@AllArgsConstructor
@Slf4j
public class RouteController {
    RouteService routeService;
    @GetMapping()
    public Route getRoute() {
        return null;
    }
    @PostMapping("/route")
    public ResponseEntity<?> createRoute(@Valid @RequestBody RouteDTO routeDTO) {
        if (routeDTO.getCarrierId() == 0 || routeDTO.getPointOfDeparture() == null || routeDTO.getDestination() == null ||
                routeDTO.getTripDuration() == 0) {
            return ResponseEntity.badRequest().body("CarrierId, Departure, Destination, and TripDuration cannot be null.");
        }

        // Если валидация прошла успешно, выполните создание билета
        return ResponseEntity.ok(routeService.createRoute(routeDTO));
    }
    @PutMapping("/updateRoute")
    public ResponseEntity<?> updateRoute(@RequestParam Integer routeId,
                                           @RequestParam(required = false) String departure,
                                           @RequestParam(required = false) String destination,
                                           @RequestParam(required = false) Integer carrierId,
                                           @RequestParam(required = false) Integer tripDuration


    ) {
        // Если валидация прошла успешно, выполните создание билета
        return ResponseEntity.ok(routeService.updateRoute(routeId, departure, destination, carrierId, tripDuration));
    }
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
