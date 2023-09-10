package com.example.ticket.configurations;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@RequiredArgsConstructor
public class WebSpringSecurityConfig {



    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {


        return http.csrf().disable()
                .authorizeHttpRequests().antMatchers("/tickets/buyTicket").authenticated()
                .antMatchers("/user/helloSecured").authenticated()
                .antMatchers("/tickets/deleteTicket").hasRole("ADMIN")
                .antMatchers("/user/admin").hasRole("ADMIN")
                .antMatchers("/user/updateUser").hasRole("ADMIN")
                .antMatchers("/carrier/**").hasRole("ADMIN")
                .antMatchers("/route/**").hasRole("ADMIN")
                .and().formLogin().and().build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    }





