package by.pasha.je.jdbc.dao;

import by.pasha.je.jdbc.dto.TicketFilter;
import by.pasha.je.jdbc.entity.Ticket;

import java.util.List;
import java.util.Optional;

public interface Dao<K,E,F> {
    void update(E ticket);

    E save(E ticket);

    boolean delete(K id);

    List<E> findALL();

    List<E> findALL(F filter);

    Optional<E> find(K id);
}
