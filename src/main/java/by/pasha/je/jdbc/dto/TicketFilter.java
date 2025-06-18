package by.pasha.je.jdbc.dto;


import java.math.BigDecimal;

public record TicketFilter(String passenger_name,
                           Integer seat_no,
                           BigDecimal сost,
                           Integer flight_id,
                           String passport_no,
                           int limit,
                           int offset) {


}
