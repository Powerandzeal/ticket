package com.example.ticket.services;

import com.example.ticket.DBUtils;
import com.example.ticket.models.Carrier;
import com.example.ticket.models.Route;
import com.example.ticket.models.Ticket;
import com.example.ticket.models.User;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class CrudUtils {

    public User createUser(User user) {


        String createQuerry = "INSERT INTO users (fullname,password,login) VALUES (?, ?, ?)";

        try (Connection connection = DBUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(createQuerry)) {
            preparedStatement.setString(1, user.getFullName());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getLogin());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;

    }
    public boolean checkUser(User user) {
        String checkQuerry = "SELECT * FROM users where login = ?";
        boolean haveHas = false;
        try (Connection connection = DBUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(checkQuerry)) {
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.executeQuery();

            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.wasNull()){
                haveHas = true;
                System.out.println(haveHas);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return haveHas;

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

    //public List<Ticket> getAllTicket() {
//    String query = "SELECT * FROM ticket";
//    List<Ticket> tickets = new ArrayList<>();
//
//    try (Connection connection = DBUtils.getConnection();
//         PreparedStatement preparedStatement = connection.prepareStatement(query)) {
//
//        ResultSet resultSet = preparedStatement.executeQuery();
//
//        while (resultSet.next()) {
//            int id = resultSet.getInt("id");
//            int routeId = resultSet.getInt("route_id"); // Получаем ID маршрута
//            Date dateDepart = resultSet.getDate("date_depart");
//            int seatNumber = resultSet.getInt("seat_number");
//            int price = resultSet.getInt("price");
//
//            // Дополнительный SQL-запрос для получения данных о маршруте
//            String routeQuery = "SELECT * FROM route WHERE id = ?";
//            try (PreparedStatement routeStatement = connection.prepareStatement(routeQuery)) {
//                routeStatement.setInt(1, routeId);
//                ResultSet routeResult = routeStatement.executeQuery();
//
//                if (routeResult.next()) {
//                    int routeIdvalue = routeResult.getInt("id");
//                    String pointOfDeparture = routeResult.getString("point_of_departure");
//                    String destination = routeResult.getString("destination");
//                    int carrierId = routeResult.getInt("carrier_id");
//                    int tripDuration = routeResult.getInt("trip_duration");
//
//                    String carrierQuerry = "SELECT * FROM carrier WHERE id = ?";
//                    try(PreparedStatement carrierStatement = connection.prepareStatement(carrierQuerry)){
//                        carrierStatement.setInt(1,carrierId);
//                        ResultSet carrierResult = carrierStatement.executeQuery();
//                        if (carrierResult.next()) {
//                            int idcarrier = carrierResult.getInt("id");
//                            String nameCarrier= carrierResult.getString("name");
//                            int phoneCarrier = carrierResult.getInt("phone_number");
//
//                            //Создаем обьект Carrier
//                            Carrier carrier = new Carrier(idcarrier,nameCarrier,phoneCarrier );
//
//                            // Создаем объект Route
//                            Route route = new Route(routeIdvalue,pointOfDeparture,destination,carrier,tripDuration);
//
//                            // Создаем объект Ticket с маршрутом
//                            tickets.add(new Ticket(id, route, dateDepart, seatNumber, price));
//
//                        }
//                    }
//
//                }
//            }
//        }
//    } catch (SQLException e) {
//        e.printStackTrace();
//    }
//
//    return tickets;
//}
    public List<Ticket> getAllTicketWithFilter(int page, int pageSize, LocalDate dateTimeFilter, String departureFilter, String destinationFilter, String carrierFilter) {
        // SQL-запрос с фильтрами и пагинацией
        System.out.println("from getall ticket " +dateTimeFilter);

        String query2 = "SELECT * FROM ticket t " +
                "JOIN route r ON t.route_id = r.id " +
                "JOIN carrier c ON r.carrier_id = c.id " +
                "WHERE " +
                "(CAST(? AS DATE) IS NULL OR CAST(t.date_depart AS DATE) = ?) " +
                "AND (? IS NULL OR r.point_of_departure LIKE ?) " +
                "AND (? IS NULL OR r.destination LIKE ?) " +
                "AND (? IS NULL OR c.name LIKE ?) " +
                "LIMIT ? OFFSET ?";
        //(? IS NOT NULL AND CAST(t.date_depart AS DATE) = ?)
        //SELECT * FROM ticket WHERE CAST(date_depart AS DATE) = ?";
        List<Ticket> tickets = new ArrayList<>();

        try (Connection connection = DBUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query2)) {



            // Установка параметров фильтров и пагинации
            preparedStatement.setDate(1, dateTimeFilter != null ? Date.valueOf(dateTimeFilter) : null);
            preparedStatement.setDate(2, dateTimeFilter != null ? Date.valueOf(dateTimeFilter) : null);
            preparedStatement.setString(3, departureFilter != null ? "%" + departureFilter + "%" : null);
            preparedStatement.setString(4, departureFilter != null ? "%" + departureFilter + "%" : null);
            preparedStatement.setString(5, destinationFilter != null ? "%" + destinationFilter + "%" : null);
            preparedStatement.setString(6, destinationFilter != null ? "%" + destinationFilter + "%" : null);
            preparedStatement.setString(7, carrierFilter != null ? "%" + carrierFilter + "%" : null);
            preparedStatement.setString(8, carrierFilter != null ? "%" + carrierFilter + "%" : null);
            preparedStatement.setInt(9, pageSize);
            preparedStatement.setInt(10, (page - 1) * pageSize);
            ResultSet resultSet = preparedStatement.executeQuery();


            while (resultSet.next()) {
                {
                    int id = resultSet.getInt("id");
                    int routeId = resultSet.getInt("route_id"); // Получаем ID маршрута
                    LocalDateTime dateDepart = resultSet.getTimestamp("date_depart").toLocalDateTime();
                    System.out.println(dateDepart);
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
                            try (PreparedStatement carrierStatement = connection.prepareStatement(carrierQuerry)) {
                                carrierStatement.setInt(1, carrierId);
                                ResultSet carrierResult = carrierStatement.executeQuery();
                                if (carrierResult.next()) {
                                    int idcarrier = carrierResult.getInt("id");
                                    String nameCarrier = carrierResult.getString("name");
                                    int phoneCarrier = carrierResult.getInt("phone_number");

                                    //Создаем обьект Carrier
                                    Carrier carrier = new Carrier(idcarrier, nameCarrier, phoneCarrier);

                                    // Создаем объект Route
                                    Route route = new Route(routeIdvalue, pointOfDeparture, destination, carrier, tripDuration);

                                    // Создаем объект Ticket с маршрутом
                                    tickets.add(new Ticket(id, route, dateDepart, seatNumber, price,null));


                                }
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

    public List<Ticket> getTicketByData(LocalDate date){
        String query = "SELECT * FROM ticket WHERE CAST(date_depart AS DATE) = ?";
    List<Ticket> tickets = new ArrayList<>();
        System.out.println("привет из getTicketByData- дата = "+ date.toString());
//
    try (Connection connection = DBUtils.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(query)) {
        preparedStatement.setDate(1, Date.valueOf(date));
        System.out.println(date);
        ResultSet resultSet = preparedStatement.executeQuery();
//
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            int routeId = resultSet.getInt("route_id"); // Получаем ID маршрута
            LocalDateTime dateDepart = resultSet.getTimestamp("date_depart").toLocalDateTime();
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
                            tickets.add(new Ticket(id, route, dateDepart, seatNumber, price,null));

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

//    public Ticket buy(int ticketId,int userID) {
//        //Запрос который достает билет по id
//        String checkQuerry = "SELECT * FROM TICKET WHERE ID = ?";
//        String insertOwner = "INSER INTO TICKET (OWNER) VALUES (?)";
//        try (Connection connection = DBUtils.getConnection();
//             PreparedStatement preparedStatement = connection.prepareStatement(checkQuerry)) {
//            preparedStatement.setInt(1, ticketId);
//            ResultSet routeResult = preparedStatement.executeQuery();
//                Integer value =  routeResult.getInt("owner");
//            if (value.equals(null)) {
//
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
public String buyTicket(int ticketId, int userId) {
    // Проверяем, существует ли билет с указанным ID
    String checkQuery = "SELECT * FROM ticket WHERE id = ?";
    String updateQuery = "UPDATE ticket SET owner_id = ? WHERE id = ?";

    try (Connection connection = DBUtils.getConnection();
         PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {

        checkStatement.setInt(1, ticketId);
        ResultSet resultSet = checkStatement.executeQuery();

        if (resultSet.next()) {
            // Билет с указанным ID существует

            // Проверяем, есть ли уже у билета владелец
            int currentOwnerId = resultSet.getInt("owner_id");
            if (currentOwnerId == 0) { // Предположим, что отсутствие владельца обозначается как 0
                // Устанавливаем нового владельца для билета
                try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                    updateStatement.setInt(1, userId);
                    updateStatement.setInt(2, ticketId);
                    updateStatement.executeUpdate();
                }

                // Создаем объект билета и возвращаем его


                // Здесь вы можете заполнить остальные поля билета
                return "Билет успешно куплен";
            } else {
                // У билета уже есть владелец, необходимо обработать эту ситуацию
                throw new IllegalStateException("Билет уже имеет владельца");
            }
        } else {
            // Билет с указанным ID не найден
            throw new IllegalArgumentException("Билет с ID " + ticketId + " не существует");
        }
    } catch (SQLException e) {
        e.printStackTrace();
        // Обработайте исключение по вашим потребностям
        return null; // или бросьте свое исключение
    }
}

//    public List<Ticket> getTicketByDepartByDestination(String pointDepart,String pointDestination,int page,int pagesize){
//        String query = "select * from ticket inner join route on ticket.route_id = route.id " +
//                "inner join carrier on route.carrier_id = carrier.id  " +
//                "where route.point_of_departure = ? " +
//                "LIMIT ? OFFSET ?" ;
//        List<Ticket> tickets = new ArrayList<>();
//        System.out.println("привет из getTicketByDepartByDestination = "+  pointDepart + pointDestination);
////
//        try (Connection connection = DBUtils.getConnection();
//             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
//            preparedStatement.setString(1,pointDepart);
//            preparedStatement.setInt(2,pagesize);
//            preparedStatement.setInt(3,page);
//            ResultSet resultSet = preparedStatement.executeQuery();
////
//            while (resultSet.next()) {
//                int id = resultSet.getInt("id");
//                int routeId = resultSet.getInt("route_id"); // Получаем ID маршрута
//                Timestamp dateDepart = resultSet.getTimestamp("date_depart");
//                int seatNumber = resultSet.getInt("seat_number");
//                int price = resultSet.getInt("price");
//
//                // Дополнительный SQL-запрос для получения данных о маршруте
//                String routeQuery = "SELECT * FROM route WHERE id = ?";
//                try (PreparedStatement routeStatement = connection.prepareStatement(routeQuery)) {
//                    routeStatement.setInt(1, routeId);
//                    ResultSet routeResult = routeStatement.executeQuery();
//
//                    if (routeResult.next()) {
//                        int routeIdvalue = routeResult.getInt("id");
//                        String pointOfDeparture = routeResult.getString("point_of_departure");
//                        String destination = routeResult.getString("destination");
//                        int carrierId = routeResult.getInt("carrier_id");
//                        int tripDuration = routeResult.getInt("trip_duration");
//
//                        String carrierQuerry = "SELECT * FROM carrier WHERE id = ?";
//                        try(PreparedStatement carrierStatement = connection.prepareStatement(carrierQuerry)){
//                            carrierStatement.setInt(1,carrierId);
//                            ResultSet carrierResult = carrierStatement.executeQuery();
//                            if (carrierResult.next()) {
//                                int idcarrier = carrierResult.getInt("id");
//                                String nameCarrier= carrierResult.getString("name");
//                                int phoneCarrier = carrierResult.getInt("phone_number");
//
//                                //Создаем обьект Carrier
//                                Carrier carrier = new Carrier(idcarrier,nameCarrier,phoneCarrier );
//
//                                // Создаем объект Route
//                                Route route = new Route(routeIdvalue,pointOfDeparture,destination,carrier,tripDuration);
//                                System.out.println(route);
//                                // Создаем объект Ticket с маршрутом
//                                Ticket ticket = new Ticket(id, route, dateDepart, seatNumber, price);
//                                System.out.println(ticket);
//                                tickets.add(ticket);
//                                System.out.println(carrier);
//                            }
//                        }
//
//                    }
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return tickets;
//    }



}





