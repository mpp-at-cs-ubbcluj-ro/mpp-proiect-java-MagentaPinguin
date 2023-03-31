package start.service.interfaces;

import start.service.ServiceException;

public interface IOfficeService {

    boolean login(String username, String password) throws ServiceException;
}
