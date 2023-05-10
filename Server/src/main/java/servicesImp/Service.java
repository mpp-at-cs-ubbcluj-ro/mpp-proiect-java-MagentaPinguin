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

    private final IOfficeRepository officeRepository;
    private final IParticipantRepository participantRepository;
    private final ITrialRepository trialRepository;
    private final IEnrolledRepository enrolledRepository;

    private Map<Long, IObserver> clients;


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
    public synchronized void logout(Office office) throws ServiceException {
        System.out.println(office);
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
    public void addParticipant(String name, String cnp, int age) throws ServiceException {
        try {
            if( participantRepository.findByCnp(cnp).isEmpty()){
                participantRepository.add(new Participant(name, cnp, age));
                var added_p=participantRepository.findByCnp(cnp);
                notifyAboutParticipants(added_p.get());
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
    public List<Trial> GetEnrollmentsFor(long id_p) throws  ServiceException{
        try {
            var par=participantRepository.find(id_p).get();
            return enrolledRepository.getTrialsFor(par);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void addEnroll(long id_p, long id_t) throws ServiceException {
        try {
            var p=participantRepository.find(id_p).get();
            var t=trialRepository.find(id_t).get();
            enrolledRepository.add(new Enrolled(p,t));
            notifyAboutEnrollments();
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    private void notifyAboutEnrollments() {
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
                        chatClient.updateTrials(this.getTrials());
                    } catch (Exception e) {
                        System.out.println("Error notifying friend " + e);
                    }
                });

        }
        executor.shutdown();
    }

    @Override
    public List<Participant> getEnrolledAt(long id_trial) throws ServiceException {
        try {
            var trial=trialRepository.find(id_trial).get();
            return enrolledRepository.getEnrolledAt(trial);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }


}
