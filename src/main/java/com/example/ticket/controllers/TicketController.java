package com.example.ticket.controllers;

import com.example.ticket.Dto.TicketCreateDTO;
import com.example.ticket.models.Ticket;
import com.example.ticket.Dto.TicketDto;
import com.example.ticket.services.TicketService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@Tag(name = "Ticket controller", description = "Просмотр, покупка, и crud операции")
@RestController
@RequestMapping("/ticket")
@AllArgsConstructor
@Slf4j
@Validated
public class TicketController {

    private final TicketService ticketService;

    /**
     * Купить билет.
     *
     * @param ticketId       Идентификатор билета.
     * @param authentication Информация об аутентификации пользователя.
     * @return Результат операции покупки билета.
     */
    @Operation(
            operationId = "buyTicket",
            summary = "Купить билет",
            description = "Покупает билет с заданным идентификатором.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            }
    )
    @PostMapping("/ticket")
    public String buyTicket(@RequestParam(value = "ticketId")
                            @Parameter(description = "Идентификатор ,билета") int ticketId,
                            Authentication authentication) {
        log.info(authentication.getName());
        return ticketService.buyTicket(ticketId, authentication);
    }

    /**
     * Создать билет.
     *
     * @param ticket Данные для создания билета.
     * @return ResponseEntity с результатом создания билета.
     */
    @Operation(
            operationId = "createTicket",
            summary = "Создать билет",
            description = "Создает новый билет на основе предоставленных данных.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "400", description = "Bad Request")
            }
    )
    @PostMapping("/ticketCreate")
    public ResponseEntity<?> createTicket(@Valid @RequestBody TicketCreateDTO ticket) {
        if (ticket.getRouteId() == 0 || ticket.getDateDepart() == null || ticket.getSeatNumber() == 0 || ticket.getPrice() == 0) {
            return ResponseEntity.badRequest().body("Route ID, Date Departure, Seat Number, and Price cannot be null.");
        }

        // Если валидация прошла успешно, выполните создание билета
        return ResponseEntity.ok(ticketService.createTicket(ticket));
    }

    /**
     * Обновить билет.
     *
     * @param ticketId   Идентификатор билета, который нужно обновить.
     * @param routeId    Новый идентификатор маршрута для билета (опционально).
     * @param data       Новая дата отправления для билета (опционально).
     * @param seatNumber Новый номер места для билета (опционально).
     * @param price      Новая цена для билета (опционально).
     * @param ownerId    Новый идентификатор владельца билета (опционально).
     * @return ResponseEntity с результатом обновления билета.
     */
    @Operation(
            operationId = "updateTicket",
            summary = "Обновить билет",
            description = "Обновляет существующий билет на основе предоставленных данных.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "400", description = "Bad Request")
            }
    )
    @PutMapping("/updateTickets")
    public ResponseEntity<?> updateTicket2(@RequestParam Integer ticketId,
                                           @RequestParam(required = false) Integer routeId,
                                           @RequestParam(required = false) @Parameter(example = "2023-09-10T12:24:07") String data,
                                           @RequestParam(required = false) Integer seatNumber,
                                           @RequestParam(required = false) Integer price,
                                           @RequestParam(required = false) Integer ownerId

    ) {
        if (price <= 0 || seatNumber <= 0 || ownerId <= 0) {
            return ResponseEntity.badRequest().body("price, seatNumber, Destination, and ownerId не могут быть отрицательными или равным 0.");
        }

        return ResponseEntity.ok(ticketService.editTicket(ticketId, routeId, data, seatNumber, price, ownerId));
    }

    /**
     * Удалить билет по идентификатору.
     *
     * @param ticketId Идентификатор билета для удаления.
     * @return ResponseEntity с результатом удаления билета.
     */
    @Operation(
            operationId = "deleteTicket",
            summary = "Удалить билет",
            description = "Удаляет билет по заданному идентификатору.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            }
    )
    @DeleteMapping("/deleteTicket")
    public ResponseEntity<String> deleteTicket(@RequestParam(value = "ticketId") int ticketId) {
        boolean isDeleted = ticketService.deleteTicketById(ticketId);

        if (isDeleted) {
            return ResponseEntity.ok("Билет успешно удален");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Билет с указанным идентификатором не найден");
        }
    }


    /**
     * Получить билеты с фильтрацией.
     *
     * @param page              Номер страницы.
     * @param pageSize          Размер страницы.
     * @param dateTimeFilter    Фильтр по дате и времени.
     * @param departureFilter   Фильтр по пункту отправления.
     * @param destinationFilter Фильтр по пункту назначения.
     * @param carrierFilter     Фильтр по перевозчику.
     * @return Список билетов, удовлетворяющих заданным параметрам фильтрации и пагинации.
     */
    @Operation(
            operationId = "getAllTicketsWithFilter",
            summary = "Получить билеты с фильтрацией",
            description = "Возвращает список билетов с учетом заданных параметров фильтрации и пагинации.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = {
                            @Content(mediaType = "*/*", schema = @Schema(implementation = Ticket.class))
                    }),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            })
    @GetMapping("/findTicket")
    public List<TicketDto> getAllTicketsWithFilter(
            @RequestParam(value = "page", defaultValue = "1")
            @Parameter(description = "Номер страницы") int page,

            @RequestParam(value = "pageSize", defaultValue = "10")
            @Parameter(description = "Размер страницы") int pageSize,

            @RequestParam(value = "dateTimeFilter", required = false)
            @Parameter(description = "Дата отправления")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTimeFilter,

            @RequestParam(value = "departureFilter", required = false)
            @Parameter(description = "Точка отправления") String departureFilter,

            @RequestParam(value = "destinationFilter", required = false)
            @Parameter(description = "Точка назначения") String destinationFilter,

            @RequestParam(value = "carrierFilter", required = false)
            @Parameter(description = "Перевозчик") String carrierFilter
    ) {

        return ticketService.getAllTicketWithFilter(page, pageSize, dateTimeFilter, departureFilter, destinationFilter, carrierFilter);
    }


}
