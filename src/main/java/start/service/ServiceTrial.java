package start.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import start.domain.Trial;
import start.repository.RepositoryException;
import start.repository.interfaces.ITrialRepository;
import start.service.interfaces.ITrialService;

import java.util.List;

public class ServiceTrial implements ITrialService {

    private static final Logger log = LogManager.getLogger();
    private final ITrialRepository trialRepository;
    public ServiceTrial(ITrialRepository repository) {
        trialRepository=repository;
    }

    public List<Trial> getTrials() throws ServiceException {
        log.traceEntry();
        try {
            return trialRepository.getAll();
        } catch (RepositoryException e) {

            throw new ServiceException(e);
        }
    }

    public void addTrial(String text, int min, int max) throws ServiceException {
          try{
              if(trialRepository.getSpecificTrial(text,min,max).isPresent())
                  throw new ServiceException("Trial already exist");
              trialRepository.add(new Trial(text,min,max));

          }catch (RepositoryException e){
                throw new ServiceException(e);
          }

    }
}