package com.example.ticket.services;

import com.example.ticket.models.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceSecurity implements UserDetailsService {
    private final CrudUtils crudUtils;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = crudUtils.findUserByLogin(username).orElseThrow(()-> new UsernameNotFoundException("User not found" + username));
        return user;
    }
}
