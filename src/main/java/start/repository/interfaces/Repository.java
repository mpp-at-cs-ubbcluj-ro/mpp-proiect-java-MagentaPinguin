package start.repository.interfaces;

import start.domain.Entity;
import start.repository.RepositoryException;

import java.util.List;
import java.util.Optional;

public interface Repository<ID, E extends Entity<ID>> {

    void add(E item) throws RepositoryException;

    void delete(ID itemID)throws RepositoryException;

    void update(E item)throws RepositoryException;

    Optional<E> find(ID itemID)throws RepositoryException;

    List<E> getAll()throws RepositoryException;
}
