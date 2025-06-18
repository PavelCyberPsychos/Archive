package by.pasha.je.jdbc.dao;

import by.pasha.je.jdbc.dto.TicketFilter;
import by.pasha.je.jdbc.entity.Ticket;
import by.pasha.je.jdbc.utils.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class TicketDao implements Dao<Integer, Ticket> {
    private final static TicketDao INSTANCE = new TicketDao();
    String SAVE_SQL = """
                    INSERT INTO ticket(cost,flight_id,passenger_name,passport_no,seat_no)
            VALUES (?,?,?,?,?)
            """;
    String DELETE_SQL = """
                    DELETE FROM ticket WHERE id=?
            """;
    String FIND_ALL_SQL = """
                        select id,cost,flight_id,passenger_name,passport_no,seat_no 
            from ticket
            """;
    String FIND_SQL = """
                        select id,cost,flight_id,passenger_name,passport_no,seat_no
            from  ticket
            where id=?
            
            """;
    String UPDATE_SQL = """
                    UPDATE ticket SET 
             cost=?,
             flight_id=?,
             passenger_name=?,
             passport_no=?,
             seat_no=?
            WHERE id=?
            """;

    public void update(Ticket ticket) {
        try (var connection = ConnectionManager.get(); var statement = connection.prepareStatement(UPDATE_SQL)) {
            statement.setBigDecimal(1, ticket.getCost());
            statement.setInt(2, ticket.getFlight_id());
            statement.setString(3, ticket.getPassenger_name());
            statement.setString(4, ticket.getPassport_no());
            statement.setInt(5, ticket.getSeat_no());
            statement.setInt(6, ticket.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    public Ticket save(Ticket ticket) {
        try (var connection = ConnectionManager.get(); var statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            statement.setBigDecimal(1, ticket.getCost());
            statement.setInt(2, ticket.getFlight_id());
            statement.setString(3, ticket.getPassenger_name());
            statement.setString(4, ticket.getPassport_no());
            statement.setInt(5, ticket.getSeat_no());

            statement.executeUpdate();
            var KEY = statement.getGeneratedKeys();
            if (KEY.next())
                ticket.setId(KEY.getInt("id"));
            return ticket;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public boolean delete(Integer id) {
        try (var connection = ConnectionManager.get(); var statement = connection.prepareStatement(DELETE_SQL)) {
            statement.setInt(1, id);
            statement.executeUpdate();
            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Ticket> findALL() {
        try (var connection = ConnectionManager.get(); var statement = connection.prepareStatement(FIND_ALL_SQL)) {
            List<Ticket> tickets = new ArrayList<>();
            var result = statement.executeQuery();

            while (result.next()) {
                tickets.add(
                        getTicket(result)
                );
            }

            return tickets;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Ticket> findALL(TicketFilter filter) {
        List<Object> parameters = new ArrayList<>();
        List<String> whereSQL = new ArrayList<>();

        if (filter.passenger_name() != null) {
            parameters.add(filter.passenger_name());
            whereSQL.add("passenger_name = ?");
        }

        if (filter.seat_no() != null) {
            parameters.add(filter.seat_no());
            whereSQL.add("seat_no = ?"); // Обратите внимание: seat_no = ?
        }
        if (filter.сost() != null) {
            parameters.add(filter.сost());
            whereSQL.add("cost = ?");
        }
        if (filter.flight_id() != null) {
            parameters.add(filter.flight_id());
            whereSQL.add("flight_id= ?");
        }
        if (filter.passport_no() != null) {
            parameters.add(filter.passport_no());
            whereSQL.add("passport_no = ?");
        }

        String whereClause = whereSQL.isEmpty() ? "" : "WHERE " + String.join(" AND ", whereSQL);


        String sql = FIND_ALL_SQL + " " + whereClause + " LIMIT ? OFFSET ?";

        System.out.println("SQL: " + sql);

        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(sql)) {

            int parameterIndex = 1;
            for (Object parameter : parameters) {
                statement.setObject(parameterIndex++, parameter);
            }

            // Добавляем limit и offset ПОСЛЕ цикла
            statement.setInt(parameterIndex++, filter.limit());
            statement.setInt(parameterIndex, filter.offset());

            System.out.println("Statement: " + statement);

            ResultSet result = statement.executeQuery();

            List<Ticket> tickets = new ArrayList<>();
            while (result.next()) {
                tickets.add(getTicket(result));
            }

            return tickets;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static Ticket getTicket(ResultSet result) throws SQLException {
        return new Ticket(result.getInt("id"),
                result.getInt("flight_id"),
                result.getBigDecimal("cost"),
                result.getString("passenger_name"),
                result.getInt("seat_no"), result.getString("passport_no")
        );
    }

    public Optional<Ticket> find(Integer id) {
        try (var connection = ConnectionManager.get(); var statement = connection.prepareStatement(FIND_SQL)) {
            statement.setInt(1, id);
            var result = statement.executeQuery();
            Ticket ticket = null;
            if (result.next()) {
                ticket = getTicket(result);
            }
            return Optional.ofNullable(ticket);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static TicketDao getINSTANCE() {
        return INSTANCE;
    }

    private TicketDao() {

    }
}
