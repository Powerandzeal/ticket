package com.example.ticket.controllers;

import com.example.ticket.Dto.TicketDto;
import com.example.ticket.Dto.UserDto;
import com.example.ticket.models.Ticket;
import com.example.ticket.models.User;
import com.example.ticket.security.UserDetailsServiceImpl;
import com.example.ticket.services.TicketService;
import com.example.ticket.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name="UserController", description="Регистрация пользователя")
@Slf4j
@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private final TicketService ticketService;
    private final UserService userService;
    private final UserDetailsServiceImpl userDetailsService;


    @GetMapping("/helloSecured")
    @PreAuthorize("hasRole('USER')")
    public String helloSec() {
        return "authorized user ";
    }

    @GetMapping("/admin")
    public String hellonotSec() {
        return "hello Admin ";
    }
    @GetMapping("/hello")
    public String hello() {
        return "hello not authorized user ";
    }
////
//    @GetMapping("/helloUser")
//    public String helloUser() {
//        return "hello  " + userService.findUserByLogin("roma") +
//                "spring security" + userDetailsService.loadUserByUsername("roma");
//    }
//
    @GetMapping("/helloUser")
    public String hellAdmin(Authentication authentication) {
        System.out.println(" hello "+ authentication.getName());
        return "Hello "+ authentication.getName();
    }
@Operation(
        operationId = "createUser",
        summary = "Создать нового пользователя",
        description = "Создает нового пользователя и возвращает его.",
        responses = {
                @ApiResponse(responseCode = "200", description = "OK", content = {
                        @Content(mediaType = "*/*", schema = @Schema(implementation = User.class))
                }),
                @ApiResponse(responseCode = "401", description = "Unauthorized"),
                @ApiResponse(responseCode = "403", description = "Forbidden"),
                @ApiResponse(responseCode = "404", description = "Not Found")
        }
)
    /**
     * Создает нового пользователя.
     *
     * @param user Объект пользователя, который будет создан.
     * @return Созданный пользователь или null, если пользователь уже существует.
     */
@PostMapping("/register")
public ResponseEntity<?> registerUser(@Valid @RequestBody UserDto user) {
    if (userService.userIsCreated(user)) {
        log.info("User is created: " + user);
        userService.createUser(user);
        return ResponseEntity.ok("Пользователь успешно зарегистрирован");
    }
    return ResponseEntity.badRequest().body("Пользователь с таким именем уже существует");
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
    public List<TicketDto> getMyTicket(Authentication authentication) {
        return ticketService.getMyTicket(authentication);
    }
    @PutMapping("/updateUser")
    public ResponseEntity<?> updateTicket2(@RequestParam Integer userId,
                                           @RequestParam(required = false) String fullName,
                                           @RequestParam(required = false) String password,
                                           @RequestParam(required = false) String login,
                                           @RequestParam(required = false) String role

    ) {

        // Если валидация прошла успешно, выполните создание билета
        return ResponseEntity.ok(userService.editUser(userId, fullName, password, login, role));
    }

//    @PatchMapping()
//    public User updateUser() {
//        return null;
//    }
//    @DeleteMapping()
//    public User deleteUser() {
//        return null;
//    }

}
