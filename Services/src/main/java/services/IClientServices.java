package services;

import model.DtoTrial;
import model.Office;
import model.Participant;
import model.Trial;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public interface IClientServices {
        Office login(Office office, IObserver observer) throws  ServiceException;
        void logout(Office office, IObserver observer) throws  ServiceException;
        List<Participant> getParticipants() throws ServiceException;
        List<DtoTrial> getTrials()throws ServiceException;

        void addParticipant(Participant p) throws  ServiceException;

        int getTrialsFor(Participant p) throws  ServiceException;

        void addEnroll(Participant p, Trial t) throws ServiceException;

        List<Participant> getEnrolledAt(Trial trial) throws ServiceException;
}
