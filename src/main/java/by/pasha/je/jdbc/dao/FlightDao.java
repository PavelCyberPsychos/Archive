package by.pasha.je.jdbc.dao;

import by.pasha.je.jdbc.dto.FlightFilter;
import by.pasha.je.jdbc.entity.Flight;
import by.pasha.je.jdbc.entity.FlightStatus;
import by.pasha.je.jdbc.entity.Ticket;
import by.pasha.je.jdbc.utils.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FlightDao implements Dao<Integer, Flight, FlightFilter> {
    private static final FlightDao INSTANCE = new FlightDao();

    private FlightDao() {
    }

    public static FlightDao getInstance() {
        return INSTANCE;
    }


    String UPDATE_SQL = """
                            UPDATE flight set 
                    aircraft_id=?,
                    arrival_airport_code=?,
                    arrival_date=?,
                    departure_airport_code=?,
                    departure_date=?,
                    flight_no=?
                    status = ? 
            """;
    String SAVE_SQL = """
            INSERT INTO flight(flight_no,departure_date,departure_airport_code,arrival_date,arrival_airport_code,aircraft_id,status)
            VALUES(?,?,?,?,?,?,?)
            """;
    String DELETE_SQL = """
            DELETE FROM flight WHERE id=?
            """;
    String FIND_ALL_SQL = """
              SELECT id,flight_no,departure_date,departure_airport_code,arrival_date,
                        arrival_airport_code,aircraft_id,status FROM flight
            """;
    String FIND_SQL = FIND_ALL_SQL + """
                                  WHERE id = ?
            """;


    @Override
    public void update(Flight flight) {
        try (var connection = ConnectionManager.get(); var statement = connection.prepareStatement(UPDATE_SQL)) {
            statement.setObject(1, flight.getAircraft_id());
            statement.setObject(2, flight.getArrival_airport_code());
            statement.setObject(3, flight.getArrival_date());
            statement.setObject(4, flight.getDeparture_airport_code());
            statement.setObject(5, flight.getDeparture_date());
            statement.setObject(6, flight.getFlight_no());
            statement.setObject(7, flight.getStatus());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Flight save(Flight flight) {
        try (var connection = ConnectionManager.get(); var statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, flight.getFlight_no());
            statement.setTimestamp(2, Timestamp.valueOf(flight.getDeparture_date()));
            statement.setString(3, flight.getDeparture_airport_code());
            statement.setTimestamp(4, Timestamp.valueOf(flight.getArrival_date()));
            statement.setString(5, flight.getArrival_airport_code());
            statement.setInt(6, flight.getAircraft_id());
            statement.setString(7, flight.getStatus().toString());
            statement.executeUpdate();
            var KEY = statement.getGeneratedKeys();
            if (KEY.next()) {
                flight.setId(KEY.getInt("id"));
            }
            return flight;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public boolean delete(Integer id) {
        try (var connection = ConnectionManager.get(); var statement = connection.prepareStatement(DELETE_SQL)) {
            statement.setInt(1, id);
            statement.executeUpdate();
            return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Flight> findALL() {
        try (var connection = ConnectionManager.get(); var statement = connection.prepareStatement(FIND_ALL_SQL)) {
            List<Flight> flights = new ArrayList<>();
            var result = statement.executeQuery();
            while (result.next()) {
                flights.add(getFlight(result));
            }
            return flights;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<Flight> findALL(FlightFilter filter) {
        return List.of();
    }

    @Override
    public Optional<Flight> find(Integer id) {
        try (var connection = ConnectionManager.get()) {
            return find(id, connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public Optional<Flight> find(Integer id, Connection connection) {
        try (var statement = connection.prepareStatement(FIND_SQL)) {
            statement.setInt(1, id);
            var result = statement.executeQuery();
            Flight flight = null;
            while (result.next()) {
                flight = getFlight(result);
            }

            return Optional.ofNullable(flight);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static Flight getFlight(ResultSet resultSet) throws SQLException {
        return new Flight(resultSet.getInt("id"),
                resultSet.getInt("aircraft_id"),
                resultSet.getString("arrival_airport_code"),
                resultSet.getTimestamp("arrival_date").toLocalDateTime(),
                resultSet.getString("departure_airport_code"),
                resultSet.getTimestamp("departure_date").toLocalDateTime(),
                resultSet.getString("flight_no"),
                FlightStatus.valueOf(resultSet.getString("status")));
    }
}
