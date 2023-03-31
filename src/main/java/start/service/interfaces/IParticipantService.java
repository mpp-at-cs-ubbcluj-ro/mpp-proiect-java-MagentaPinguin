package start.service.interfaces;

import start.domain.Participant;
import start.service.ServiceException;

import java.util.List;

public interface IParticipantService {
    List<Participant> getParticipants() throws ServiceException;

    void addParticipant(String inputFullName, String inputCnp, int inputAge) throws ServiceException;

}