package com.example.ticket.services;

import com.example.ticket.Dto.TicketDto;
import com.example.ticket.Dto.UserDto;
import com.example.ticket.configurations.DBConfig;
import com.example.ticket.configurations.Role;
import com.example.ticket.models.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {

    private final PasswordEncoder passwordEncoder;

    /**
     * Создает нового пользователя и сохраняет его в базу данных.
     *
     * @param user Пользователь для создания.
     * @return Созданный пользователь.
     */

    public UserDto createUser(UserDto user) {

        String createQuerry = "INSERT INTO users (fullname,password,login,role) VALUES (?, ?, ?,?)";

        try (Connection connection = DBConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(createQuerry)) {
            preparedStatement.setString(1, user.getFullName());
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            preparedStatement.setString(2, encodedPassword);
            preparedStatement.setString(3, user.getLogin());
            preparedStatement.setString(4, "USER");
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;

    }

    /**
     * Ищет пользователя по логину в базе данных.
     *
     * @param login Логин пользователя.
     * @return Найденный пользователь или пустое значение, если пользователь не найден.
     */
    public Optional<User> findUserByLogin(String login) {
        User user1 = null;
        String findQuerry = "SELECT * FROM USERS WHERE LOGIN = ?";
        try (Connection connection = DBConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(findQuerry)) {
            preparedStatement.setString(1, login);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {

                String fullName = resultSet.getString("fullName");

                int id = resultSet.getInt("id");
                String password = resultSet.getString("password");
                String login1 = resultSet.getString("login");
                Role role = Role.valueOf(resultSet.getString("role"));
                user1 = new User(id, fullName, password, login1, role);
                log.info(String.valueOf(user1));

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(user1);
    }

    /**
     * Проверяет, создан ли пользователь с данным логином.
     *
     * @param user Пользователь для проверки.
     * @return true, если пользователь с данным логином уже существует, в противном случае - false.
     */
    public boolean userIsCreated(UserDto user) {
        log.info(user.getLogin());
        String checkQuerry = "SELECT * FROM users where login = ?";
        boolean haveHas = false;
        try (Connection connection = DBConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(checkQuerry)) {
            preparedStatement.setString(1, user.getLogin());


            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                resultSet.getString("login").equals(user.getLogin());
                log.info("пользователь c таким логином уже существует");

            } else haveHas = true;


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return haveHas;
    }

    public String editUser(int userId, String fullName, String password, String login, String role) {
        String checkQuery = "SELECT * FROM ticket WHERE id = ?";
        String updateQuery = "UPDATE users SET ";

        try (Connection connection = DBConfig.getConnection();
             PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {

            checkStatement.setInt(1, userId);
            ResultSet resultSet = checkStatement.executeQuery();

            if (resultSet.next()) {
                StringBuilder updateSql = new StringBuilder(updateQuery);
                boolean needsComma = false;

                if (fullName != null) {
                    updateSql.append("fullname = ?");
                    needsComma = true;
                }

                if (password != null) {
                    if (needsComma) {
                        updateSql.append(", ");
                    }
                    updateSql.append("password = ?");
                    needsComma = true;
                }

                if (login != null) {
                    if (needsComma) {
                        updateSql.append(", ");
                    }
                    updateSql.append("login = ?");
                    needsComma = true;
                }

                if (role != null) {
                    if (needsComma) {
                        updateSql.append(", ");
                    }
                    updateSql.append("role = ?");
                    needsComma = true;
                }

                updateSql.append(" WHERE id = ?");

                try (PreparedStatement updateStatement = connection.prepareStatement(updateSql.toString())) {
                    int parameterIndex = 1;

                    if (fullName != null) {
                        updateStatement.setString(parameterIndex++, fullName);
                    }

                    if (password != null) {
                        String encodedPassword = passwordEncoder.encode(password);
                        updateStatement.setString(parameterIndex++, encodedPassword);
                    }

                    if (login != null) {
                        updateStatement.setString(parameterIndex++, login);
                    }
                    if (role != null) {
                        updateStatement.setString(parameterIndex++, role);
                    }

                    updateStatement.setInt(parameterIndex, userId);

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "Данные пользователя изменены";
    }

}
