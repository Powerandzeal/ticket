package com.example.ticket.services;

import com.example.ticket.Dto.CarrierDto;
import com.example.ticket.configurations.DBConfig;
import com.example.ticket.models.Carrier;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Service
public class CarrierService {
    public String createCarrier(CarrierDto routeDTO) {
        String createQuerry = "INSERT INTO carrier (name, phone_number) VALUES (?, ?)";

        try (Connection connection = DBConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(createQuerry)) {
            preparedStatement.setString(1, routeDTO.getName());
            preparedStatement.setInt(2, routeDTO.getPhoneNumber());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {

                return "Маршрут успешно создан";
            } else {
                // Обработка случая, когда билет не был добавлен
                return "Ошибка создания маршрута";
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public Carrier updateCarrier(int carrierId, String nameCarrier, Integer phoneNumber) {
        String checkQuery = "SELECT * FROM carrier WHERE id = ?";
        String updateQuery = "UPDATE carrier SET ";

        try (Connection connection = DBConfig.getConnection();
             PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {

            checkStatement.setInt(1, carrierId);
            ResultSet resultSet = checkStatement.executeQuery();

            if (resultSet.next()) {
                StringBuilder updateSql = new StringBuilder(updateQuery);
                boolean needsComma = false;

                if (nameCarrier != null) {
                    updateSql.append("name = ?");
                    needsComma = true;
                }

                if (phoneNumber != null) {
                    if (needsComma) {
                        updateSql.append(", ");
                    }
                    updateSql.append("phone_number = ?");
                    needsComma = true;
                }


                updateSql.append(" WHERE id = ?");

                try (PreparedStatement updateStatement = connection.prepareStatement(updateSql.toString())) {
                    int parameterIndex = 1;


                    if (nameCarrier != null) {
                        updateStatement.setString(parameterIndex++, nameCarrier);
                    }

                    if (phoneNumber != null) {
                        updateStatement.setInt(parameterIndex++, phoneNumber);
                    }

                    updateStatement.setInt(parameterIndex, carrierId);

                    int rowsAffected = updateStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
    public boolean deleteCarrierById(int carrierId) {
        String deleteQuery = "DELETE FROM carrier WHERE id = ?";
        try (Connection connection = DBConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {

            preparedStatement.setInt(1, carrierId);
            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0; // Если удаление прошло успешно, вернуть true
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
