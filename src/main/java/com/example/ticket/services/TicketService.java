package com.example.ticket.services;

import com.example.ticket.Dto.TicketDto;
import com.example.ticket.configurations.DBConfig;
import com.example.ticket.models.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Schema(name = "Сервис билетов",description = "взаимодействие с билетами")
@Service
@AllArgsConstructor
public class TicketService {
    /**
     * Покупает билет для пользователя с указанным ID.
     *
     * @param ticketId Идентификатор билета.
     *                 authentication Аутентификация пользователя.
     * @return Строка с сообщением о результате операции.
     */
    public String buyTicket(int ticketId, Authentication login) {
        // Проверяем, существует ли билет с указанным ID
        String checkQuery = "SELECT * FROM ticket WHERE id = ?";
        String updateQuery = "UPDATE ticket SET owner = ? WHERE id = ?";
        String findUser = "select * from users where login = ?";
        int userId = 0;
        try (Connection connection = DBConfig.getConnection();
             PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {

            checkStatement.setInt(1, ticketId);
            ResultSet resultSet = checkStatement.executeQuery();

            if (resultSet.next()) {
                // Билет с указанным ID существует

                // Проверяем, есть ли уже у билета владелец
                int currentOwnerId = resultSet.getInt("owner");
                if (currentOwnerId == 0) { // Предположим, что отсутствие владельца обозначается как 0
                    // Устанавливаем нового владельца для билета
                    try (PreparedStatement preparedStatement = connection.prepareStatement(findUser)) {
                        preparedStatement.setString(1, login.getName());
                        ResultSet resultSet1 = preparedStatement.executeQuery();
                        if (resultSet1.next()) {
                            userId = resultSet1.getInt("id");
                        }
                    }
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
                    return "Билет уже имеет владельца";
                    //throw new IllegalStateException("Билет уже имеет владельца");

                }
            } else {
                // Билет с указанным ID не найден
                return "Билет с ID " + ticketId + " не существует";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Обработайте исключение по вашим потребностям
            return "покупка"; // или бросьте свое исключение
        }
    }

    /**
     * Получает список билетов, принадлежащих пользователю.
     *
     * @param authentication Аутентификация пользователя.
     * @return Список билетов пользователя.
     */
    public List<TicketDto> getMyTicket(Authentication authentication) {
        String login = authentication.getName();
        String findQuerry = "select * FROM ticket join users on ticket.owner = users.id where users.login = ? ";

        List<TicketDto> tickets = new ArrayList<>();


        try (Connection connection = DBConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(findQuerry)) {
            preparedStatement.setString(1, login);
            System.out.println(login);
            ResultSet resultSet = preparedStatement.executeQuery();

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
                                Ticket ticket = new Ticket(id, route, dateDepart, seatNumber, price, null);
                                TicketDto ticketDto = new TicketDto(id,pointOfDeparture,destination,nameCarrier,
                                        tripDuration,seatNumber,price,dateDepart    );
                                tickets.add(ticketDto);

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

    /**
     * Получает список билетов с фильтрацией по различным параметрам.
     *
     * @param page              Номер страницы.
     * @param pageSize          Размер страницы.
     * @param dateTimeFilter    Фильтр по дате и времени.
     * @param departureFilter   Фильтр по пункту отправления.
     * @param destinationFilter Фильтр по пункту назначения.
     * @param carrierFilter     Фильтр по перевозчику.
     * @return Список билетов, отфильтрованных согласно заданным параметрам.
     */
    public List<TicketDto> getAllTicketWithFilter(int page, int pageSize, LocalDate dateTimeFilter, String departureFilter, String destinationFilter, String carrierFilter) {
        // SQL-запрос с фильтрами и пагинацией
        System.out.println("from getall ticket " + dateTimeFilter);

        String query2 = "SELECT * FROM ticket t " +
                "JOIN route r ON t.route_id = r.id " +
                "JOIN carrier c ON r.carrier_id = c.id " +
                "WHERE " +
                "(CAST(? AS DATE) IS NULL OR CAST(t.date_depart AS DATE) = ?) " +
                "AND (? IS NULL OR r.point_of_departure LIKE ?) " +
                "AND (? IS NULL OR r.destination LIKE ?) " +
                "AND (? IS NULL OR c.name LIKE ?) " +
                "LIMIT ? OFFSET ?";

        List<TicketDto> tickets = new ArrayList<>();

        try (Connection connection = DBConfig.getConnection();
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
                                    Ticket ticket = new Ticket(id, route, dateDepart, seatNumber, price, null);
                                    TicketDto ticketDto = new TicketDto(id,pointOfDeparture,destination,nameCarrier,
                                            tripDuration,seatNumber,price,dateDepart    );
                                    tickets.add(ticketDto);

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

//    public void deleteTicketById(int ticketId) {
//        String deleteQuerry = "DELETE FROM ticket where id = ?";
//        try (Connection connection = DBConfig.getConnection();
//             PreparedStatement checkStatement = connection.prepareStatement(deleteQuerry)) {
//
//            checkStatement.setInt(1, ticketId);
//            ResultSet resultSet = checkStatement.executeQuery();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
public boolean deleteTicketById(int ticketId) {
    String deleteQuery = "DELETE FROM ticket WHERE id = ?";
    try (Connection connection = DBConfig.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {

        preparedStatement.setInt(1, ticketId);
        int rowsAffected = preparedStatement.executeUpdate();

        return rowsAffected > 0; // Если удаление прошло успешно, вернуть true
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
}

//    public Ticket findTicketbyId(int ticketId, String userName) {
//        String findTicketQuerry = "SELECT * FROM ticket WHERE id = ?";
//
//        try (Connection connection = DBConfig.getConnection();
//             PreparedStatement checkStatement = connection.prepareStatement(findTicketQuerry)) {
//
//            checkStatement.setInt(1, ticketId);
//            ResultSet resultSet = checkStatement.executeQuery();
//
//            if (resultSet.next()) {
//                {
//                    {
//                        int id = resultSet.getInt("id");
//                        int routeId = resultSet.getInt("route_id"); // Получаем ID маршрута
//                        LocalDateTime dateDepart = resultSet.getTimestamp("date_depart").toLocalDateTime();
//                        System.out.println(dateDepart);
//                        int seatNumber = resultSet.getInt("seat_number");
//                        int price = resultSet.getInt("price");
//
//                        // Дополнительный SQL-запрос для получения данных о маршруте
//                        String routeQuery = "SELECT * FROM route WHERE id = ?";
//                        try (PreparedStatement routeStatement = connection.prepareStatement(routeQuery)) {
//                            routeStatement.setInt(1, routeId);
//                            ResultSet routeResult = routeStatement.executeQuery();
//
//                            if (routeResult.next()) {
//                                int routeIdvalue = routeResult.getInt("id");
//                                String pointOfDeparture = routeResult.getString("point_of_departure");
//                                String destination = routeResult.getString("destination");
//                                int carrierId = routeResult.getInt("carrier_id");
//                                int tripDuration = routeResult.getInt("trip_duration");
//
//                                String carrierQuerry = "SELECT * FROM carrier WHERE id = ?";
//                                try (PreparedStatement carrierStatement = connection.prepareStatement(carrierQuerry)) {
//                                    carrierStatement.setInt(1, carrierId);
//                                    ResultSet carrierResult = carrierStatement.executeQuery();
//                                    if (carrierResult.next()) {
//                                        int idcarrier = carrierResult.getInt("id");
//                                        String nameCarrier = carrierResult.getString("name");
//                                        int phoneCarrier = carrierResult.getInt("phone_number");
//                                        String findUserQuerry = "SELECT * FROM users WHERE login = ?";
//                                        try (PreparedStatement userStatement = connection.prepareStatement(findUserQuerry)) {
//                                            userStatement.setString(1, userName);
//                                            ResultSet userResult = userStatement.executeQuery();
//                                            if (userResult.next()) {
//                                                int idUser = userResult.getInt("id");
//                                                String password = userResult.getString("password");
//                                                String login = userResult.getString("login");
//                                                String fullName = userResult.getString("fullname");
//                                                Role role = Role.valueOf(userResult.getString("role"));
//                                                //Создаем обьект Carrier
//                                                Carrier carrier = new Carrier(idcarrier, nameCarrier, phoneCarrier);
//
//                                                // Создаем объект Route
//                                                Route route = new Route(routeIdvalue, pointOfDeparture, destination, carrier, tripDuration);
//                                                User user = new User(idUser, password, login, fullName, role);
//                                                // Создаем объект Ticket с маршрутом
//                                                return new Ticket(id, route, dateDepart, seatNumber, price, user);
//                                            }
//
//                                        }
//
//                                    }
//
//                                }
//                            }
//                        }
//                    }
//                }
//            } else {
//                // Билет с указанным ID не найден
//                return null;
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            // Обработайте исключение по вашим потребностям
//            return null; // или бросьте свое исключение
//        }
//        return null;
//    }

}

