package repository;

import model.*;

import java.util.Optional;

public interface IOfficeRepository extends Repository<Long, Office> {
    Optional<Office> findByUsername( String username)throws RepositoryException;

}
