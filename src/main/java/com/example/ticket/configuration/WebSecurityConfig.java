package com.example.ticket.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {



//    @Bean
//    public UserDetailsManager users(DataSource dataSource) {
//        UserDetails user = User.withDefaultPasswordEncoder()
//                .username("user")
//                .password("password")
//                .roles("USER")
//                .build();
//        JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
//        users.createUser(user);
//        return users;
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {


        return http.httpBasic().and()
                .authorizeHttpRequests().antMatchers("/tickets/buyTicket").authenticated()
                .antMatchers("/user/helloSecured").authenticated()
                .and().formLogin().and().build();
    }


//    @Bean
//    public JdbcUserDetailsManager usersFromDb(DataSource dataSource) {
//        UserDetails user = User.builder()
//                .username("user")
//                .password("1234")
//                .roles("USER")
//                .build();
//
//        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
//        jdbcUserDetailsManager.createUser(user);
//        return jdbcUserDetailsManager;
//    }


//


}
