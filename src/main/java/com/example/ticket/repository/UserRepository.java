//package com.example.ticket.repository;
//
//import com.example.ticket.models.User;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public class UserRepository {
//    private final JdbcTemplate jdbcTemplate;
//
//    public UserRepository(JdbcTemplate jdbcTemplate) {
//        this.jdbcTemplate = jdbcTemplate;
//    }
//
//    public User findById(Long id) {
//        String sql = "SELECT * FROM users WHERE id = ?";
//        return jdbcTemplate.queryForObject(sql, new Object[]{id}, new UserRowMapper());
//    }
//
//    public List<User> findAll() {
//        String sql = "SELECT * FROM users";
//        return jdbcTemplate.query(sql, new UserRowMapper());
//    }
//
//    public void save(User user) {
//        String sql = "INSERT INTO users (username, email) VALUES (?, ?)";
//        jdbcTemplate.update(sql, user.getUsername(), user.getEmail());
//    }
//
//    public void update(User user) {
//        String sql = "UPDATE users SET username = ?, email = ? WHERE id = ?";
//        jdbcTemplate.update(sql, user.getUsername(), user.getEmail(), user.getId());
//    }
//
//    public void delete(Long id) {
//        String sql = "DELETE FROM users WHERE id = ?";
//        jdbcTemplate.update(sql, id);
//    }
//}
