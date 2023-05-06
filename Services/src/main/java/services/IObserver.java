package services;

import model.DtoTrial;
import model.Participant;

import java.util.List;

public interface IObserver {
    void updateTrials(List<DtoTrial> newList) throws ServiceException;

    void   updateParticipants(Participant p) throws ServiceException;
}
