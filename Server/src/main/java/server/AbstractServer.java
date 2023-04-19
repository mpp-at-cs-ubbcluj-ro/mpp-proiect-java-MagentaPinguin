package server;

import services.ServiceException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class AbstractServer {
    private int port;
    private ServerSocket server=null;
    public AbstractServer( int port){
        this.port=port;
    }

    public void start() throws ServiceException {
        try{
            server=new ServerSocket(port);
            while(true){
                System.out.println("Waiting for clients ...");
                Socket client=server.accept();
                System.out.println("Client connected ...");
                processRequest(client);
            }
        } catch (IOException e) {
            throw new ServiceException("Starting server errror ",e);
        }finally {
            stop();
        }
    }

    public void stop() throws ServiceException {
        try {
            server.close();
        } catch (IOException e) {
            throw new ServiceException("Closing server error ", e);
        }
    }
    protected abstract  void processRequest(Socket client);

}
