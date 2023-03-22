package start.repository.interfaces;

import start.domain.Participant;
import start.repository.RepositoryException;

import java.util.Optional;

public interface IParticipantRepository extends Repository<Long,Participant> {
    Optional<Participant> findByCnp(String cnp)throws RepositoryException;

}
