package by.pasha.je.jdbc;

import by.pasha.je.jdbc.dao.FlightDao;
import by.pasha.je.jdbc.dao.TicketDao;
import by.pasha.je.jdbc.dto.TicketFilter;
import by.pasha.je.jdbc.entity.Flight;
import by.pasha.je.jdbc.entity.FlightStatus;
import by.pasha.je.jdbc.entity.Ticket;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class JdbcRunner {
    public static void main(String[] args) throws SQLException {
        var ticketDao = TicketDao.getINSTANCE();
        System.out.println(ticketDao.find(3));


    }
}


