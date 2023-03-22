package start.repository.interfaces;


import start.domain.Trial;
import start.repository.RepositoryException;

import java.util.List;
import java.util.Optional;

public interface ITrialRepository extends Repository<Long, Trial>{

    Optional<Trial> findByName(String name) throws RepositoryException;
    List<Trial> getAllForAge(int age) throws RepositoryException;
}
