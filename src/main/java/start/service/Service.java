package start.service;


import start.domain.Participant;
import start.domain.Trial;
import start.service.dtos.DtoTrial;
import start.service.interfaces.*;

import java.util.List;


public class Service implements IService {

    private final IOfficeService serviceOffice;
    private final IParticipantService serviceParticipant;
    private final ITrialService serviceTrial;
    private final IEnrollmentService serviceEnrollment;

    public Service(ServiceOffice serviceOffice, ServiceParticipant serviceParticipant, ServiceTrial serviceTrial, ServiceEnrollment serviceEnrollment) {
        this.serviceOffice = serviceOffice;
        this.serviceParticipant = serviceParticipant;
        this.serviceTrial = serviceTrial;
        this.serviceEnrollment = serviceEnrollment;
    }

    public boolean login(String username, String passwd) throws ServiceException {
        return serviceOffice.login(username,passwd);
    }

    public List<DtoTrial> getTrials() throws ServiceException {

       return serviceTrial.getTrials()
                .stream()
                .map(e-> {
                    try{
                        return new DtoTrial(e,serviceEnrollment.getEnrolledAt(e).size());
                    }catch (ServiceException ex) {
                    throw new RuntimeException(ex);
                }}).toList();

    }

    public List<Participant> getParticipants() throws ServiceException {
        return serviceParticipant.getParticipants();
    }

    public List<Trial> getTrialsFor(Participant p) throws ServiceException {
        return serviceEnrollment.getTrialsFor(p);
    }

    public List<Participant> getEnrolledAt(Trial t) throws ServiceException {
        return serviceEnrollment.getEnrolledAt(t);
    }


    public void addParticipant(String inputFullName, String inputCnp, String inputAge) throws ServiceException{
        if(inputFullName.isEmpty() ||inputCnp.isEmpty() ||inputAge.isEmpty())
            throw new ServiceException("Missing arguments");

        serviceParticipant.addParticipant(inputFullName,
                inputCnp,
                Integer.parseInt(inputAge));
    }

    public void addEnrollment(Participant p, Trial t) throws ServiceException{
            if(serviceEnrollment.getTrialsFor(p).contains(t)) {
                var str = "Already enrolled at " + t.getName();
                throw new ServiceException(str);
            }
            serviceEnrollment.addEnrollment(p,t);


    }
}
