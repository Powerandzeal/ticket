package com.example.ticket.services;

import com.example.ticket.DBUtils;
import com.example.ticket.models.Carrier;
import com.example.ticket.models.Route;
import com.example.ticket.models.Ticket;
import com.example.ticket.models.User;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.*;
import java.util.Date;

@Service
public class CrudUtils {

    public User createUser(User user) {
        String createQuerry = "INSERT INTO users (fullname,password,login) VALUES (?, ?, ?)";

        try (Connection connection = DBUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(createQuerry))
        {
            preparedStatement.setString(1,user.getFullName());
            preparedStatement.setString(2,user.getPassword());
            preparedStatement.setString(3,user.getLogin());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;

    }
//public User saveUser(User user) {
//    try (Connection connection = DBUtils.getConnection();
//         PreparedStatement preparedStatement = connection.prepareStatement(
//                 "INSERT INTO users (login, password,fullName) VALUES (?, ?,?)",
//                 Statement.RETURN_GENERATED_KEYS)) {
//        preparedStatement.setString(0, user.getLogin());
//        preparedStatement.setString(1, user.getPassword());
//        preparedStatement.setString(2,user.getFullName());
//        preparedStatement.executeUpdate();
//        try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
//            if (generatedKeys.next()) {
//                user.setId(generatedKeys.getInt(0));
//            }
//        }
//    } catch (SQLException e) {
//        e.printStackTrace();
//    }
//    return user;
//}

public List<Ticket> getAllTicket() {
    String query = "SELECT * FROM ticket";
    List<Ticket> tickets = new ArrayList<>();

    try (Connection connection = DBUtils.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(query)) {

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            int routeId = resultSet.getInt("route_id"); // Получаем ID маршрута
            Date dateDepart = resultSet.getDate("date_depart");
            int seatNumber = resultSet.getInt("seat_number");
            int price = resultSet.getInt("price");

            // Дополнительный SQL-запрос для получения данных о маршруте
            String routeQuery = "SELECT * FROM route WHERE id = ?";
            try (PreparedStatement routeStatement = connection.prepareStatement(routeQuery)) {
                routeStatement.setInt(1, routeId);
                ResultSet routeResult = routeStatement.executeQuery();

                if (routeResult.next()) {
                    int routeIdvalue = routeResult.getInt("id");
                    String pointOfDeparture = routeResult.getString("point_of_departure");
                    String destination = routeResult.getString("destination");
                    int carrierId = routeResult.getInt("carrier_id");
                    int tripDuration = routeResult.getInt("trip_duration");

                    String carrierQuerry = "SELECT * FROM carrier WHERE id = ?";
                    try(PreparedStatement carrierStatement = connection.prepareStatement(carrierQuerry)){
                        carrierStatement.setInt(1,carrierId);
                        ResultSet carrierResult = carrierStatement.executeQuery();
                        if (carrierResult.next()) {
                            int idcarrier = carrierResult.getInt("id");
                            String nameCarrier= carrierResult.getString("name");
                            int phoneCarrier = carrierResult.getInt("phone_number");

                            //Создаем обьект Carrier
                            Carrier carrier = new Carrier(idcarrier,nameCarrier,phoneCarrier );

                            // Создаем объект Route
                            Route route = new Route(routeIdvalue,pointOfDeparture,destination,carrier,tripDuration);

                            // Создаем объект Ticket с маршрутом
                            tickets.add(new Ticket(id, route, dateDepart, seatNumber, price));

                        }
                    }

                }
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return tickets;
}
    public List<Ticket> getAllTicketWithPagination(int page) {
        String query = "SELECT * FROM ticket";
        List<Ticket> tickets = new ArrayList<>();

        try (Connection connection = DBUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int routeId = resultSet.getInt("route_id"); // Получаем ID маршрута
                Date dateDepart = resultSet.getDate("date_depart");
                int seatNumber = resultSet.getInt("seat_number");
                int price = resultSet.getInt("price");

                // Дополнительный SQL-запрос для получения данных о маршруте
                String routeQuery = "SELECT * FROM route WHERE id = ?";
                try (PreparedStatement routeStatement = connection.prepareStatement(routeQuery)) {
                    routeStatement.setInt(1, routeId);
                    ResultSet routeResult = routeStatement.executeQuery();

                    if (routeResult.next()) {
                        int routeIdvalue = routeResult.getInt("id");
                        String pointOfDeparture = routeResult.getString("point_of_departure");
                        String destination = routeResult.getString("destination");
                        int carrierId = routeResult.getInt("carrier_id");
                        int tripDuration = routeResult.getInt("trip_duration");

                        String carrierQuerry = "SELECT * FROM carrier WHERE id = ?";
                        try(PreparedStatement carrierStatement = connection.prepareStatement(carrierQuerry)){
                            carrierStatement.setInt(1,carrierId);
                            ResultSet carrierResult = carrierStatement.executeQuery();
                            if (carrierResult.next()) {
                                int idcarrier = carrierResult.getInt("id");
                                String nameCarrier= carrierResult.getString("name");
                                int phoneCarrier = carrierResult.getInt("phone_number");

                                //Создаем обьект Carrier
                                Carrier carrier = new Carrier(idcarrier,nameCarrier,phoneCarrier );

                                // Создаем объект Route
                                Route route = new Route(routeIdvalue,pointOfDeparture,destination,carrier,tripDuration);

                                // Создаем объект Ticket с маршрутом
                                tickets.add(new Ticket(id, route, dateDepart, seatNumber, price));

                            }
                        }

                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tickets;
    }


}





