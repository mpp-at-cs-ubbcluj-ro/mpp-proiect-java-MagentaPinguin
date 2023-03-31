package start.service;

import start.domain.Participant;
import start.repository.RepositoryException;
import start.repository.interfaces.IParticipantRepository;
import start.service.interfaces.IParticipantService;

import java.util.List;

public class ServiceParticipant implements IParticipantService {

    private IParticipantRepository participantRepository;
    public ServiceParticipant(IParticipantRepository repository) {
        participantRepository=repository;
    }
    public List<Participant> getParticipants() throws ServiceException {
        try {
            return participantRepository.getAll();
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public void addParticipant(String inputFullName, String inputCnp, int inputAge) throws ServiceException {

        try {
            if(participantRepository.findByCnp(inputCnp).isPresent())
                throw new ServiceException("There is someone with this CNP ");
            participantRepository.add(new Participant(inputFullName,inputCnp,inputAge));
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }
}
