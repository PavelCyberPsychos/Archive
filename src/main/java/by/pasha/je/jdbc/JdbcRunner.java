package by.pasha.je.jdbc;

import by.pasha.je.jdbc.dao.TicketDao;
import by.pasha.je.jdbc.dto.TicketFilter;
import by.pasha.je.jdbc.entity.Ticket;

import java.math.BigDecimal;
import java.sql.SQLException;

public class JdbcRunner {
    public static void main(String[] args) throws SQLException {
        var ticketDao = TicketDao.getINSTANCE();
        var filter = new TicketFilter(null, null, null, null, null, 3, 0);
        System.out.println(ticketDao.findALL(filter));

    }
}


