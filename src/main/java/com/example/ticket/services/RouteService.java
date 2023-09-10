package com.example.ticket.services;

import com.example.ticket.Dto.RouteDTO;
import com.example.ticket.configurations.DBConfig;
import com.example.ticket.models.Carrier;
import com.example.ticket.models.Route;
import org.springframework.stereotype.Service;

import java.sql.*;

@Service
public class RouteService {
    public String createRoute(RouteDTO routeDTO) {
        String createQuerry = "INSERT INTO route (point_of_departure, destination," +
                " carrier_id, trip_duration) VALUES (?, ?, ?, ?)";

        try (Connection connection = DBConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(createQuerry)) {
            preparedStatement.setString(1, routeDTO.getPointOfDeparture());
            preparedStatement.setString(2, routeDTO.getDestination());
            preparedStatement.setInt(3,routeDTO.getCarrierId());
            preparedStatement.setInt(4, routeDTO.getTripDuration());
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
    public boolean deleteRouteById(int routeId) {
        String deleteQuery = "DELETE FROM route WHERE id = ?";
        try (Connection connection = DBConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {

            preparedStatement.setInt(1, routeId);
            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0; // Если удаление прошло успешно, вернуть true
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public Route updateRoute(int routeId, String pointOfDeparture, String destination, Integer carrierId,
                             Integer tripDuration) {
        String checkQuery = "SELECT * FROM route WHERE id = ?";
        String updateQuery = "UPDATE route SET ";

        try (Connection connection = DBConfig.getConnection();
             PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {

            checkStatement.setInt(1, routeId);
            ResultSet resultSet = checkStatement.executeQuery();

            if (resultSet.next()) {
                StringBuilder updateSql = new StringBuilder(updateQuery);
                boolean needsComma = false;

                if (pointOfDeparture != null) {
                    updateSql.append("point_of_departure = ?");
                    needsComma = true;
                }

                if (destination != null) {
                    if (needsComma) {
                        updateSql.append(", ");
                    }
                    updateSql.append("destination = ?");
                    needsComma = true;
                }

                if (carrierId != null) {
                    if (needsComma) {
                        updateSql.append(", ");
                    }
                    updateSql.append("carrier_id = ?");
                    needsComma = true;
                }

                if (tripDuration != null) {
                    if (needsComma) {
                        updateSql.append(", ");
                    }
                    updateSql.append("trip_duration = ?");
                    needsComma = true;
                }


                updateSql.append(" WHERE id = ?");

                try (PreparedStatement updateStatement = connection.prepareStatement(updateSql.toString())) {
                    int parameterIndex = 1;


                    if (pointOfDeparture != null) {
                        updateStatement.setString(parameterIndex++, pointOfDeparture);
                    }

                    if (destination != null) {
                        updateStatement.setString(parameterIndex++, destination);
                    }
                    if (carrierId != null) {
                        updateStatement.setInt(parameterIndex++, carrierId);
                    } else {
                        System.out.println("тут ноль");
                    }
                    if (tripDuration != null) {
                        updateStatement.setInt(parameterIndex++, tripDuration);
                    }
                    else {
                        System.out.println("тут ноль");
                    }
                    updateStatement.setInt(parameterIndex, routeId);

                    int rowsAffected = updateStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // Билет с указанным ID не найден или произошла ошибка
    }
    }

