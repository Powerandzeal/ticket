package com.example.ticket.controllers;

import com.example.ticket.models.Carrier;
import com.example.ticket.models.User;
import com.example.ticket.services.CrudUtils;
import com.example.ticket.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {
   private final CrudUtils crudUtils;

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @PostMapping("createUser")
    public User createUser(@RequestBody User user) {
        if (crudUtils.checkUser(user)){
            return crudUtils.createUser(user);

        }
        return null ;
    }
    @PatchMapping()
    public User updateUser() {
        return null;
    }
    @DeleteMapping()
    public User deleteUser() {
        return null;
    }

}
