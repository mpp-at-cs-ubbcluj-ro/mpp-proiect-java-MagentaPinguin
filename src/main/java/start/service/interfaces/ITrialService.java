package start.service.interfaces;

import start.domain.Trial;
import start.repository.RepositoryException;
import start.service.ServiceException;

import java.util.List;

public interface ITrialService {

    public List<Trial> getTrials() throws ServiceException;

    public void addTrial(String text, int min, int max) throws ServiceException ;

}
