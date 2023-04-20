package start.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import start.repository.RepositoryException;
import start.repository.interfaces.IOfficeRepository;
import start.service.interfaces.IOfficeService;


public class ServiceOffice implements IOfficeService {

    private static final Logger logger = LogManager.getLogger();
    private final IOfficeRepository officeRepository;
    public ServiceOffice(IOfficeRepository repository) {
        officeRepository=repository;
    }


    @Override
    public boolean login(String username, String password) throws ServiceException {
        logger.traceEntry("Params {}",username,password);
        try {
            var found=officeRepository.findByUsername(username);
            logger.traceExit("Login exit");
            return found.isPresent() && found.get().getPassword().equals(password);
        }catch (RepositoryException ex){
            throw logger.throwing(new ServiceException(ex));
        }
    }
}
