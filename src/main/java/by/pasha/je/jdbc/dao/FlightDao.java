package by.pasha.je.jdbc.dao;

import by.pasha.je.jdbc.dto.FlightFilter;
import by.pasha.je.jdbc.dto.TicketFilter;
import by.pasha.je.jdbc.entity.Flight;
import by.pasha.je.jdbc.entity.Ticket;
import by.pasha.je.jdbc.utils.ConnectionManager;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class FlightDao implements Dao<Integer, Flight, FlightFilter> {
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

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Flight save(Flight flight) {
        return null;
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }

    @Override
    public List<Flight> findALL() {
        return List.of();
    }

    @Override
    public List<Flight> findALL(FlightFilter filter) {
        return List.of();
    }

    @Override
    public Optional<Flight> find(Integer id) {
        return Optional.empty();
    }
}
