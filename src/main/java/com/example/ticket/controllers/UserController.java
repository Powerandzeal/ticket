package com.example.ticket.controllers;

import com.example.ticket.models.Carrier;
import com.example.ticket.models.User;
import com.example.ticket.services.CrudUtils;
import com.example.ticket.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {
   private final CrudUtils crudUtils;


    @GetMapping("/helloSecured")
    public String helloSec() {
        return "hello Security ";
    }

//    @GetMapping("/info")
//    public String hellAdmin(Principal principal) {
//        return principal.getName();
//    }

    @PostMapping("/Registration")
    public User createUser(@RequestBody User user) {
        if (crudUtils.checkUser(user)){
            System.out.println("createUser");
            return crudUtils.createUser(user);

        }
        return null ;
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
