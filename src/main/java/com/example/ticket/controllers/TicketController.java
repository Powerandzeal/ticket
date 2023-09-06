package com.example.ticket.controllers;

import com.example.ticket.models.Ticket;
import com.example.ticket.services.TicketService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Ticket controller", description = "Просмотр и покупка билетов")
@RestController
@RequestMapping("/tickets")
@AllArgsConstructor
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
    @GetMapping("/buyTicket")
    public String buyTicket(@RequestParam(value = "ticketId")
                            @Parameter(description = "Идентификатор пользователя") int ticketId,
                            Authentication authentication) {
        System.out.println(authentication.getName());
        return ticketService.buyTicket(ticketId, authentication);
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
    @GetMapping("/FindTicketWithFiltration")
    public List<Ticket> getAllTicketsWithFilter(
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

        // Вызываем сервисный метод, передавая параметры фильтрации и пагинации
        return ticketService.getAllTicketWithFilter(page, pageSize, dateTimeFilter, departureFilter, destinationFilter, carrierFilter);
    }
    @Operation(
            operationId = "getMyTicket",
            summary = "Получить купленные билеты",
            description = "Возвращает список билетов, принадлежащих авторизованному пользователю.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = {
                            @Content(mediaType = "*/*", schema = @Schema(implementation = Ticket.class))
                    }),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            })
    /**
     * Получает список билетов, принадлежащих авторизованному пользователю.
     *
     * @param authentication Информация об авторизации пользователя.
     * @return Список билетов пользователя.
     */
    @GetMapping("/showMyTicket")
    public List<Ticket> getMyTicket(Authentication authentication) {
        return ticketService.getMyTicket(authentication);
    }

}
