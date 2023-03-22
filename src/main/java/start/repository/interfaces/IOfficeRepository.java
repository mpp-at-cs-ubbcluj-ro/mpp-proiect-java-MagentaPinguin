package start.repository.interfaces;

import start.domain.Office;
import start.repository.RepositoryException;

import java.util.Optional;

public interface IOfficeRepository extends Repository<Long, Office> {

    Optional<Office> findByUsername( String username)throws RepositoryException;

}
