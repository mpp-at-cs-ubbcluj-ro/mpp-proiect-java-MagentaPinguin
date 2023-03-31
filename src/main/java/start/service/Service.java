package start.service;

import javafx.scene.control.TextField;
import start.domain.Participant;
import start.domain.Trial;
import start.service.interfaces.IEnrollmentService;
import start.service.interfaces.IOfficeService;
import start.service.interfaces.IParticipantService;
import start.service.interfaces.ITrialService;

import java.util.List;


public class Service {

    private IOfficeService serviceOffice;
    private IParticipantService serviceParticipant;
    private ITrialService serviceTrial;
    private IEnrollmentService serviceEnrollment;

    public Service(ServiceOffice serviceOffice, ServiceParticipant serviceParticipant, ServiceTrial serviceTrial, ServiceEnrollment serviceEnrollment) {
        this.serviceOffice = serviceOffice;
        this.serviceParticipant = serviceParticipant;
        this.serviceTrial = serviceTrial;
        this.serviceEnrollment = serviceEnrollment;
    }

    public boolean login(String username, String passwd) throws ServiceException {
        return serviceOffice.login(username,passwd);
    }

    public List<Trial> getTrials() throws ServiceException {
        return serviceTrial.getTrials();
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


    public void addParticipant(TextField inputFullName, TextField inputCnp, TextField inputAge) throws ServiceException{
        if(inputFullName.getText().isEmpty() ||inputCnp.getText().isEmpty() ||inputAge.getText().isEmpty())
            throw new ServiceException("Missing arguments");

        serviceParticipant.addParticipant(inputFullName.getText(),
                inputCnp.getText(),
                Integer.parseInt(inputAge.getText()));
    }

    public void addTrial(TextField inputTrialName, TextField inputMinAge, TextField inputMaxAge) throws ServiceException {

        if(inputTrialName.getText().isEmpty() ||inputMinAge.getText().isEmpty() ||inputMaxAge.getText().isEmpty())
            throw new ServiceException("Missing arguments");
        try {
            var min = Integer.parseInt(inputMinAge.getText());
            var max = Integer.parseInt(inputMaxAge.getText());
            if (min <= 0 || max <= 0 || min>=max)
                throw new Exception("Bad arguments");
            serviceTrial.addTrial(inputTrialName.getText(),min,max);
        }catch (Exception e){
        throw new ServiceException(e);
        }

    }

    public void addEnrollment(Participant p, Trial t) throws ServiceException{
            if(serviceEnrollment.getTrialsFor(p).contains(t))
                throw new ServiceException("Already enrolled at "+t.getName());
            serviceEnrollment.addEnrollment(p,t);


    }
}
