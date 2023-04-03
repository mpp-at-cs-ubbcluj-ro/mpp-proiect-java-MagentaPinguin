package start.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import start.domain.Participant;
import start.repository.RepositoryException;
import start.repository.interfaces.IParticipantRepository;
import start.service.interfaces.IParticipantService;

import java.util.List;

public class ServiceParticipant implements IParticipantService {

    private static final Logger logger = LogManager.getLogger();
    private IParticipantRepository participantRepository;
    public ServiceParticipant(IParticipantRepository repository) {
        participantRepository=repository;
    }
    public List<Participant> getParticipants() throws ServiceException {
        logger.traceEntry("Params");
        try {
            return participantRepository.getAll();
        } catch (RepositoryException e) {
            throw logger.throwing(new ServiceException(e));

        }
    }

    public void addParticipant(String inputFullName, String inputCnp, int inputAge) throws ServiceException {
        logger.traceEntry("Params {0} {1} {2}",inputFullName, inputCnp,inputAge);
        try {
            if(participantRepository.findByCnp(inputCnp).isPresent())
                throw new ServiceException("There is someone with this CNP ");
            participantRepository.add(new Participant(inputFullName,inputCnp,inputAge));
        } catch (RepositoryException e) {
            throw logger.throwing(new ServiceException(e));

        }
    }
}
