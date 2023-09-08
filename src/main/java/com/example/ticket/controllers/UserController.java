package com.example.ticket.controllers;

import com.example.ticket.models.User;
import com.example.ticket.security.UserDetailsServiceImpl;
import com.example.ticket.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name="UserController", description="Регистрация пользователя")

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {


    private final UserService userService;
    private final UserDetailsServiceImpl userDetailsService;


    @GetMapping("/helloSecured")
    public String helloSec() {
        return "hello Security ";
    }
    @GetMapping("/helloNotSecured")
    public String hellonotSec() {
        return "hello Security ";
    }
//
    @GetMapping("/hello")
    public String hello() {
        return "hello  " + userService.findUserByLogin("roma") +
                "spring security" + userDetailsService.loadUserByUsername("roma");
    }
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
    @PostMapping("/Registration")
    public User createUser(@RequestBody User user) {
        if (userService.userIsCreated(user)) {
            System.out.println("createUser");
            return userService.createUser(user);

        }
        return null;
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
