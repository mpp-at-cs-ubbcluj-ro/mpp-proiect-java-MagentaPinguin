package start.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import start.domain.Enrolled;
import start.domain.Participant;
import start.domain.Trial;
import start.repository.RepositoryException;
import start.repository.interfaces.IEnrolledRepository;
import start.service.interfaces.IEnrollmentService;

import java.util.List;


public class ServiceEnrollment implements IEnrollmentService    {
    private static final Logger log = LogManager.getLogger();
    private IEnrolledRepository enrolledRepository;

    public ServiceEnrollment(IEnrolledRepository repository) {

        this.enrolledRepository = repository;
    }

    @Override
    public List<Trial> getTrialsFor(Participant p) throws ServiceException {
        try {
            return enrolledRepository.getTrialsFor(p);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public  List<Participant> getEnrolledAt(Trial t) throws ServiceException {
        try {
            return enrolledRepository.getEnrolledAt(t);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void addEnrollment(Participant p, Trial t) throws ServiceException{
        try {
            if (p.getAge()<t.getMinAge() || p.getAge()>t.getMaxAge())
                throw new Exception("Participant's age does not match the trial");
            enrolledRepository.add(new Enrolled(p,t));
        } catch (Exception e) {
            throw new ServiceException(e);
        }

    }
}
