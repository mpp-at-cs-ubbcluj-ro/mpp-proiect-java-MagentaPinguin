package start.service.interfaces;


import start.domain.*;
import start.service.ServiceException;
import start.service.dtos.DtoTrial;

import java.util.List;

public interface IService {

     boolean login(String username, String passwd) throws ServiceException;

     List<DtoTrial> getTrials() throws ServiceException ;

     List<Participant> getParticipants() throws ServiceException ;

     List<Trial> getTrialsFor(Participant p) throws ServiceException ;

     List<Participant> getEnrolledAt(Trial t) throws ServiceException ;


     void addParticipant(String inputFullName, String inputCnp, String inputAge) throws ServiceException;

     void addEnrollment(Participant p, Trial t) throws ServiceException;
}
