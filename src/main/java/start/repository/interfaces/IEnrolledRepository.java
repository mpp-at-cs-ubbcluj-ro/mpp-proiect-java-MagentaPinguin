package start.repository.interfaces;

import start.domain.Enrolled;
import start.domain.Participant;
import start.domain.Trial;
import start.repository.RepositoryException;

import java.util.List;


public interface IEnrolledRepository extends Repository<Long, Enrolled> {

    List<Trial> getTrialsFor(Participant participantID) throws RepositoryException;

    List<Participant> getEnrolledAt(Trial idTrial)throws RepositoryException;

}
