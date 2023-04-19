package repository;


import model.*;

import java.util.List;

public interface IEnrolledRepository extends Repository<Long, Enrolled> {

    List<Trial> getTrialsFor(Participant participantID) throws RepositoryException;

    List<Participant> getEnrolledAt(Trial idTrial)throws RepositoryException;

}
