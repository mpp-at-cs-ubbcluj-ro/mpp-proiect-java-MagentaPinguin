package start.repository;

import start.domain.Entity;

import java.util.Optional;

public interface Repository<ID, E extends Entity<ID>> {

    void add(E item);

    void delete(E item);

    void update(E item);

    Optional<E> find(ID itemID);

}
