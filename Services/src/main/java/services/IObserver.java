package services;

import model.Participant;
import model.Trial;

public interface IObserver {
    //void updateTrials(Trial t);

    void updateParticipants(Participant p) throws ServiceException;
}
