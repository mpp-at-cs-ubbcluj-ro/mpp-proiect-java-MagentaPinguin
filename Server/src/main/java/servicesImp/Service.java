package servicesImp;

import model.*;
import repository.*;
import services.IClientServices;
import services.IObserver;
import services.ServiceException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Service implements IClientServices {

    private IOfficeRepository officeRepository;
    private IParticipantRepository participantRepository;
    private ITrialRepository trialRepository;
    private IEnrolledRepository enrolledRepository;

    Map<Long, IObserver> clients;


    public Service(IOfficeRepository serviceOffice, IParticipantRepository serviceParticipant, ITrialRepository serviceTrial, IEnrolledRepository serviceEnrollment) {
        this.officeRepository = serviceOffice;
        this.participantRepository = serviceParticipant;
        this.trialRepository = serviceTrial;
        this.enrolledRepository = serviceEnrollment;
        clients=new HashMap<>();
    }

    @Override
    public synchronized Office login(Office office, IObserver observer) throws ServiceException {

        try {

            var found= officeRepository.findByUsername(office.getUsername());
            if(found.isPresent() && clients.containsKey(found.get().getId()))
                throw new ServiceException("Already logged");
            clients.put(found.get().getId(), observer);
            return found.get();

        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public synchronized void logout(Office office, IObserver observer) throws ServiceException {
        IObserver localClient=clients.remove(office.getId());
        if(localClient==null)
            throw new ServiceException("Not logged");
    }

    @Override
    public synchronized List<Participant> getParticipants() throws ServiceException {
        try {
            return participantRepository.getAll();
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public synchronized List<DtoTrial> getTrials() throws ServiceException {
        try {

            return trialRepository.getAll()
                        .stream()
                        .map(item-> {
                            try {
                                return new DtoTrial(item,enrolledRepository.getEnrolledAt(item).size());
                            } catch (RepositoryException e) {

                                throw new RuntimeException(e);
                            }
                        }).toList();
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void addParticipant(Participant p) throws ServiceException {
        try {
            if( participantRepository.findByCnp(p.getCnp()).isEmpty()){
                participantRepository.add(p);
                notifyAboutParticipants(p);
            }
        } catch (RepositoryException e) {
            throw new ServiceException(e);

        }
    }

    private void notifyAboutParticipants(Participant p) {
        Iterable<Office> offices= null;
        try {
            offices = officeRepository.getAll();
        } catch (RepositoryException e) {
            System.out.println("Error getAll office");
        }
        ExecutorService executor= Executors.newFixedThreadPool(5);
        for(Office of :offices){
            IObserver chatClient=clients.get(of.getId());
            if (chatClient!=null)
                executor.execute(() -> {
                    try {
                        System.out.println("Notifying ["+of.getId()+"] about a participant");
                        chatClient.updateParticipants(p);
                    } catch (Exception e) {
                        System.out.println("Error notifying friend " + e);
                    }
                });

        }
        executor.shutdown();
    }

    @Override
    public int getTrialsFor(Participant p) throws  ServiceException{
        try {
            return enrolledRepository.getTrialsFor(p).size();
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void addEnroll(Participant p, Trial t) throws ServiceException {
        try {
            enrolledRepository.add(new Enrolled(p,t));
            notifyAboutEnrollments();
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    private void notifyAboutEnrollments() {

    }

    @Override
    public List<Participant> getEnrolledAt(Trial trial) throws ServiceException {
        try {
            return enrolledRepository.getEnrolledAt(trial);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }


}
