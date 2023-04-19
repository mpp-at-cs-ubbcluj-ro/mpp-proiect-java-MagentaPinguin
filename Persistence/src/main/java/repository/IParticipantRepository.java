package repository;


import model.*;

import java.util.Optional;

public interface IParticipantRepository extends Repository<Long,Participant> {
    Optional<Participant> findByCnp(String cnp)throws RepositoryException;

}
