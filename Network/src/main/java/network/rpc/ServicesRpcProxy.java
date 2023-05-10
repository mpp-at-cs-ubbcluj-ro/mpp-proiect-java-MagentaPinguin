package network.rpc;


import model.DtoTrial;
import model.Office;
import model.Participant;
import model.Trial;
import services.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ServicesRpcProxy implements IClientServices {

    private final String host;
    private final int port;

    private IObserver client;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;

    private final BlockingQueue<Response> qresponses;
    private volatile boolean finished;

    public ServicesRpcProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses = new LinkedBlockingQueue<>();
    }

    @Override
    public Office login(Office office, IObserver client) throws ServiceException {
        initializeConnection();
        Request req = new Request.Builder().type(RequestType.LOGIN).data(office).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.OK) {
            this.client = client;
            return (Office) response.data();
        }
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            closeConnection();
            throw new ServiceException(err);
        }
        return null;
    }

    @Override
    public void logout(Office office) throws ServiceException {
        Request req = new Request.Builder().type(RequestType.LOGOUT).data(office).build();
        sendRequest(req);
        Response response = readResponse();
        closeConnection();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new ServiceException(err);
        }
    }

    @Override
    public List<Participant> getParticipants() throws ServiceException {
        Request req = new Request.Builder().type(RequestType.GET_PARTICIPANTS).data(null).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.OK) {

            return (List<Participant>) response.data();
        }
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new ServiceException(err);
        }
        return null;
    }

    @Override
    public List<DtoTrial> getTrials() throws ServiceException {
        Request req = new Request.Builder().type(RequestType.GET_TRIALS).data(null).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.OK) {

            return (List<DtoTrial>) response.data();
        }
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();

            throw new ServiceException(err);
        }
        return null;
    }

    @Override
    public void addParticipant(String name, String cnp, int age ) throws ServiceException {

        var list = List.of(name,cnp,age);
        Request req = new Request.Builder().type(RequestType.ADD_PARTICIPANT).data(list).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();

            throw new ServiceException(err);
        }
    }

    @Override
    public List<Trial> GetEnrollmentsFor(long id_p) throws ServiceException {
        Request req = new Request.Builder().type(RequestType.GET_TrialsFor).data(id_p).build();
        sendRequest(req);
        Response response = readResponse();

        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new ServiceException(err);
        }
        return (List<Trial>) response.data();
    }

    @Override
    public void addEnroll(long p, long t) throws ServiceException {
        Request req = new Request.Builder().type(RequestType.ADD_ENROLL).data(List.of(p, t)).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new ServiceException(err);
        }
    }

    @Override
    public List<Participant> getEnrolledAt(long id_trial) throws ServiceException {
        Request req = new Request.Builder().type(RequestType.GET_EnrolledAt).data(id_trial).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.OK) {
            return (List<Participant>) response.data();
        }
        String err = response.data().toString();
        throw new ServiceException(err);
    }



    private void sendRequest(Request request)throws ServiceException {
        try {
            output.writeObject(request);
            output.flush();
        } catch (IOException e) {
            throw new ServiceException ("Error sending object "+e);
        }

    }

    private Response readResponse() throws ServiceException {
        Response response=null;
        try{
            response=qresponses.take();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }



    private void initializeConnection() throws ServiceException {
        try {
            connection=new Socket(host,port);
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            finished=false;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void closeConnection() {
        finished=true;
        try {
            input.close();
            output.close();
            connection.close();
            client=null;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void startReader(){
        Thread tw=new Thread(new ReaderThread());
        tw.start();
    }

    private boolean isUpdate(Response response){
        return response.type()== ResponseType.ENROLL_ADDED || response.type()== ResponseType.PARTICIPANT_ADDED;
    }

    private void handleUpdate(Response response) throws ServiceException {

        if (response.type()== ResponseType.PARTICIPANT_ADDED){
            System.out.println("Update on participants");
            client.updateParticipants((Participant) response.data());
        }

        if (response.type()== ResponseType.ENROLL_ADDED){
            System.out.println("Update on enrollments");
            client.updateTrials((List<DtoTrial>) response.data());
        }


    }

    private class ReaderThread implements Runnable{
        public void run() {
            while(!finished){
                try {
                    Object response=input.readObject();
                    System.out.println("response received "+response);
                    if (isUpdate((Response)response)){
                        handleUpdate((Response)response);
                    }else{

                        try {
                            qresponses.put((Response)response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException | ClassNotFoundException | ServiceException e) {
                    System.out.println("Reading error "+e);

                }
            }
        }
    }
}
