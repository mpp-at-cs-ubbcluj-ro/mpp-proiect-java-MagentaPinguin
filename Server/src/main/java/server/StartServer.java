package server;

import repository.*;
import repository.orm.OfficeRepositoryOrm;
import services.IClientServices;
import services.ServiceException;
import servicesImp.Service;

import java.io.IOException;
import java.util.Properties;

public class StartServer {
    private static int serverPort=55555;
    public static void main(String[] args) {
        Properties serverProps=new Properties();

        try {
            serverProps.load(StartServer.class.getResourceAsStream("serverProperties.properties"));
            serverPort = Integer.parseInt(serverProps.getProperty("server.port"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find properties "+e);
            return;
        }

        System.out.println("Server on localhost port:"+serverPort);

        IOfficeRepository officeRepository=new OfficeRepositoryOrm();
        IParticipantRepository participantRepository=new ParticipantRepository(serverProps);
        ITrialRepository trialRepository=new TrialRepository(serverProps);
        IEnrolledRepository enrolledRepository=new EnrolledRepository(serverProps);

        IClientServices serverImpl=new Service(officeRepository, participantRepository,trialRepository,enrolledRepository);
        AbstractServer server = new ProtoConcurrentServer(serverPort, serverImpl);

        try {
            server.start();
        } catch (ServiceException e) {
            System.err.println("Error starting the server" + e.getMessage());
        }finally {
            try {
                OfficeRepositoryOrm.close();
                server.stop();
            }catch(ServiceException e){
                System.err.println("Error stopping server "+e.getMessage());
            }
        }
    }
}
