package services;

import model.DtoTrial;
import model.Office;
import model.Participant;
import model.Trial;


import java.util.List;

public interface IClientServices {
        Office login(Office office, IObserver observer) throws  ServiceException;
        void logout(Office office ) throws  ServiceException;
        //Log
        List<Participant> getParticipants() throws ServiceException;
        List<DtoTrial> getTrials()throws ServiceException;
        List<Trial> GetEnrollmentsFor(long  id_participant) throws  ServiceException;
        List<Participant> getEnrolledAt(long id_trial) throws ServiceException;
        //Getters
        void addParticipant(String fullname, String cnp, int age) throws  ServiceException;
        void addEnroll(long id_participant, long id_trial) throws ServiceException;
        //Add

}
