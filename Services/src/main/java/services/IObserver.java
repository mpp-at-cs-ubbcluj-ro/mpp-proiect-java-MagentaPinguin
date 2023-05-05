package services;

import model.Participant;

public interface IObserver {
    void updateTrials() throws ServiceException;

    void   updateParticipants(Participant p) throws ServiceException;
}
