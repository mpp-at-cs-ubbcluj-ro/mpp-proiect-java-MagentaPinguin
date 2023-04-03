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
    private static final Logger logger = LogManager.getLogger();
    private IEnrolledRepository enrolledRepository;

    public ServiceEnrollment(IEnrolledRepository repository) {
        logger.traceEntry("Params {0}:",repository);
        this.enrolledRepository = repository;
        logger.traceExit("ServiceEnrollment constructor exit.");
    }

    @Override
    public List<Trial> getTrialsFor(Participant p) throws ServiceException {
        logger.traceEntry("Params {0}:",p);
        try {
            logger.traceExit("Service-GetTrialsFor exit.");
            return enrolledRepository.getTrialsFor(p);
        } catch (RepositoryException e) {
            throw logger.throwing(new ServiceException(e));
        }
    }

    @Override
    public  List<Participant> getEnrolledAt(Trial t) throws ServiceException {
        logger.traceEntry("Params {0}:",t);
        try {
            logger.traceExit("Service-GetEnrolledAt exit.");
            return enrolledRepository.getEnrolledAt(t);
        } catch (RepositoryException e) {
            throw logger.throwing(new ServiceException(e));
        }
    }

    @Override
    public void addEnrollment(Participant p, Trial t) throws ServiceException{
        logger.traceEntry("Params {0}:",p,t);
        try {
            if (p.getAge()<t.getMinAge() || p.getAge()>t.getMaxAge())
                throw logger.throwing(new ServiceException("Participant's age does not match the trial"));
            enrolledRepository.add(new Enrolled(p,t));
        } catch (Exception e) {
            throw logger.throwing(new ServiceException(e));

        }

    }
}
