package by.pasha.je.jdbc.entity;

import java.math.BigDecimal;
import java.util.Objects;

public class Ticket {
    private int id;
    private BigDecimal cost;
    private Flight flight;
    private String passenger_name;
    private String passport_no;
    private int seat_no;

    public Ticket() {
    }

    public Ticket(int id, Flight flight, BigDecimal cost, String passenger_name, int seat_no, String passport_no) {
        this.id = id;
        this.flight = flight;
        this.cost = cost;
        this.passenger_name = passenger_name;
        this.seat_no = seat_no;
        this.passport_no = passport_no;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public String getPassenger_name() {
        return passenger_name;
    }

    public void setPassenger_name(String passenger_name) {
        this.passenger_name = passenger_name;
    }

    public String getPassport_no() {
        return passport_no;
    }

    public void setPassport_no(String passport_no) {
        this.passport_no = passport_no;
    }

    public int getSeat_no() {
        return seat_no;
    }

    public void setSeat_no(int seat_no) {
        this.seat_no = seat_no;
    }

    @Override
    public String toString() {
        return "Ticket{" +
               "id=" + id +
               ", cost=" + cost +
               ", flight_id=" + flight +
               ", passenger_name='" + passenger_name + '\'' +
               ", passport_no='" + passport_no + '\'' +
               ", seat_no=" + seat_no +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return id == ticket.id && flight == ticket.flight && seat_no == ticket.seat_no && Objects.equals(cost, ticket.cost) && Objects.equals(passenger_name, ticket.passenger_name) && Objects.equals(passport_no, ticket.passport_no);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cost, flight, passenger_name, passport_no, seat_no);
    }
}
