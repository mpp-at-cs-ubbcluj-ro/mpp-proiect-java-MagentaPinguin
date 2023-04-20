package start.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import start.domain.Trial;
import start.repository.RepositoryException;
import start.repository.interfaces.ITrialRepository;
import start.service.interfaces.ITrialService;

import java.util.List;

public class ServiceTrial implements ITrialService {

    private static final Logger logger = LogManager.getLogger();
    private final ITrialRepository trialRepository;

    public ServiceTrial(ITrialRepository repository) {
        trialRepository = repository;
    }

    public List<Trial> getTrials() throws ServiceException {
        logger.traceEntry("Params");
        try {
            logger.traceExit("GetTrial exit.");
            return trialRepository.getAll();
        } catch (RepositoryException e) {
            throw logger.throwing(new ServiceException(e));
        }
    }

}