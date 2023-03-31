package start.service.interfaces;

import start.domain.Participant;
import start.domain.Trial;
import start.service.ServiceException;


import java.util.List;

public interface IEnrollmentService {
     List<Trial> getTrialsFor(Participant p) throws ServiceException;

     List<Participant> getEnrolledAt(Trial t) throws ServiceException;

     void addEnrollment(Participant p, Trial t) throws ServiceException;
}
