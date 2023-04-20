package start.service.interfaces;

import start.domain.Trial;

import start.service.ServiceException;

import java.util.List;

public interface ITrialService {

     List<Trial> getTrials() throws ServiceException;

}
