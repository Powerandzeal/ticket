package com.example.ticket.services;

import com.example.ticket.DBUtils;
import com.example.ticket.configuration.Role;
import com.example.ticket.models.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
@Schema(description = "Сервис пользователей")
@Service
@AllArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;

    /**
     * Создает нового пользователя и сохраняет его в базу данных.
     *
     * @param user Пользователь для создания.
     * @return Созданный пользователь.
     */

    public User createUser(User user) {

        String createQuerry = "INSERT INTO users (fullname,password,login,role) VALUES (?, ?, ?,?)";

        try (Connection connection = DBUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(createQuerry)) {
            preparedStatement.setString(1, user.getFullName());
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            preparedStatement.setString(2, encodedPassword);
            preparedStatement.setString(3, user.getLogin());
            preparedStatement.setString(4,user.getRole().toString());
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
        try (Connection connection = DBUtils.getConnection();
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
                System.out.println(user1);

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
    public boolean userIsCreated(User user) {
        System.out.println(user.getLogin());
        String checkQuerry = "SELECT * FROM users where login = ?";
        boolean haveHas = false;
        try (Connection connection = DBUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(checkQuerry)) {
            preparedStatement.setString(1, user.getLogin());


            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                resultSet.getString("login").equals(user.getLogin());
                System.out.println("пользователь c таким логином уже существует");

            } else haveHas = true;


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return haveHas;
    }
}
