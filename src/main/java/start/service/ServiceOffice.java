package start.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import start.repository.RepositoryException;
import start.repository.interfaces.IOfficeRepository;
import start.service.interfaces.IOfficeService;


public class ServiceOffice implements IOfficeService {

    private static final Logger log = LogManager.getLogger();
    private IOfficeRepository officeRepository;
    public ServiceOffice(IOfficeRepository repository) {
        officeRepository=repository;
    }


    @Override
    public boolean login(String username, String password) throws ServiceException {
        log.traceEntry("Params {}",username,password);
        try {
            var found=officeRepository.findByUsername(username);
            return found.isPresent() && found.get().getPassword().equals(password);
        }catch (RepositoryException ex){
            throw log.throwing(new ServiceException(ex));
        }
    }
}
