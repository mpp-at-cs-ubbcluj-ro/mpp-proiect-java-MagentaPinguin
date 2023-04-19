package repository;

import model.*;

import java.util.List;
import java.util.Optional;

public interface ITrialRepository extends Repository<Long, Trial>{
   Optional<Trial> getSpecificTrial(String name, int minAge, int maxAge) throws RepositoryException;
   List<Trial> findByName(String name) throws RepositoryException;
   List<Trial> getTrialsForAge(int age) throws RepositoryException;
}
