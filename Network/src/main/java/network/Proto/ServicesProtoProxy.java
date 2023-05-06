package network.Proto;


import model.DtoTrial;
import model.Office;
import model.Participant;
import model.Trial;
import services.IClientServices;
import services.IObserver;
import services.ServiceException;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ServicesProtoProxy implements IClientServices {

    private final String host;
    private final int port;

    private IObserver client;

    private InputStream input;
    private OutputStream output;
    private Socket connection;

    private final BlockingQueue<ProtocolProtobuff.Response> qresponses;
    private volatile boolean finished;

    public ServicesProtoProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses = new LinkedBlockingQueue<>();
    }

    @Override
    public Office login(Office office, IObserver client) throws ServiceException {
        initializeConnection();
        sendRequest(ProtoUtils.creaateLoginRequest(office));
        ProtocolProtobuff.Response response = readResponse();
        if (response.getType() == ProtocolProtobuff.Response.Type.Login) {
            this.client = client;
            return  ProtoUtils.getOffice(response);
        }
        if (response.getType() == ProtocolProtobuff.Response.Type.Error) {
            String err = response.getTxt();
            closeConnection();
            throw new ServiceException(err);
        }
        return null;
    }

    @Override
    public void logout(Office office) throws ServiceException {
        ProtocolProtobuff.Request req = ProtoUtils.createLogoutRequest(office);
                //new Request.Builder().type(RequestType.LOGOUT).data(office).build();
        sendRequest(req);
        ProtocolProtobuff.Response response = readResponse();
        closeConnection();
        if (response.getType() == ProtocolProtobuff.Response.Type.Error) {
            String err = response.getTxt();
            throw new ServiceException(err);
        }
    }

    @Override
    public List<Participant> getParticipants() throws ServiceException {
        ProtocolProtobuff.Request req =
                ProtocolProtobuff.Request.newBuilder().setType(ProtocolProtobuff.Request.Type.GetParticipants).build();
        sendRequest(req);
        ProtocolProtobuff.Response response = readResponse();
        if (response.getType()== ProtocolProtobuff.Response.Type.GetParticipants) {

            return ProtoUtils.getParticipantList(response);
        }
        if (response.getType()== ProtocolProtobuff.Response.Type.Error) {
            String err = response.getTxt();
            throw new ServiceException(err);
        }
        return null;
    }

    @Override
    public List<DtoTrial> getTrials() throws ServiceException {
        ProtocolProtobuff.Request req = ProtocolProtobuff.Request.newBuilder().setType(ProtocolProtobuff.Request.Type.GetTrials).build();
        sendRequest(req);
        ProtocolProtobuff.Response response = readResponse();
        if (response.getType() == ProtocolProtobuff.Response.Type.GetTrials) {

            return ProtoUtils.getDroTrialList(response);
        }
        if (response.getType() == ProtocolProtobuff.Response.Type.Error) {
            String err = response.getTxt();
            throw new ServiceException(err);
        }
        return null;
    }

    @Override
    public void addParticipant(String name, String cnp, int age ) throws ServiceException {

        ProtocolProtobuff.Request req = ProtoUtils.createAddParticipantRequest(name, cnp, age);
        sendRequest(req);
        ProtocolProtobuff.Response response = readResponse();
        if (response.getType()== ProtocolProtobuff.Response.Type.Error) {
            String err = response.toString();
            throw new ServiceException(err);
        }
    }

    @Override
    public void addEnroll(long p, long t) throws ServiceException {

        ProtocolProtobuff.Request req = ProtoUtils.createAddEnroll(p,t);
        sendRequest(req);
        ProtocolProtobuff.Response response = readResponse();

        if (response.getType() == ProtocolProtobuff.Response.Type.Error) {
            String err = response.getTxt();
            throw new ServiceException(err);
        }
    }

    @Override
    public List<Trial> GetEnrollmentsFor(long id_p) throws ServiceException {
        ProtocolProtobuff.Request req = ProtoUtils.createGetEnrollmentsForRequest(id_p);
        sendRequest(req);
        ProtocolProtobuff.Response response = readResponse();
        if (response.getType() == ProtocolProtobuff.Response.Type.Error) {
            throw new ServiceException(response.getTxt());
        }
        return ProtoUtils.getTrialList(response);

    }

    @Override
    public List<Participant> getEnrolledAt(long id_trial) throws ServiceException {
        ProtocolProtobuff.Request req = ProtoUtils.createGetEnrolledAtRequest(id_trial);
        //new Request.Builder().type(RequestType.GET_EnrolledAt).data(id_trial).build();
        sendRequest(req);
        ProtocolProtobuff.Response  response = readResponse();
        if (response.getType() == ProtocolProtobuff.Response.Type.GetEnrolledAt) {

            return ProtoUtils.getParticipantList(response);
        }
        throw new ServiceException(response.getTxt());
    }

    private void sendRequest(ProtocolProtobuff.Request request)throws ServiceException {
        try {
            request.writeDelimitedTo(output);
            output.flush();
        } catch (IOException e) {
            throw new ServiceException ("Error sending object "+e);
        }

    }

    private ProtocolProtobuff.Response readResponse(){
        ProtocolProtobuff.Response  response=null;
        try{
            response=qresponses.take();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    private void initializeConnection(){
        try {
            connection=new Socket(host,port);
            output= connection.getOutputStream();
            output.flush();
            input=connection.getInputStream();
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

    private boolean isUpdate(ProtocolProtobuff.Response response){
        return response.getType() == ProtocolProtobuff.Response.Type.AddEnroll || response.getType() == ProtocolProtobuff.Response.Type.AddParticipant;
    }

    private void handleUpdate(ProtocolProtobuff.Response response) throws ServiceException {


        if (response.getType() == ProtocolProtobuff.Response.Type.AddParticipant){
            System.out.println("Update on participants");
            var p=ProtoUtils.getParticipant(response);
            System.out.println("\n\n"+p.getName().toUpperCase());
            client.updateParticipants(p);
        }

        if (response.getType() == ProtocolProtobuff.Response.Type.AddEnroll){
            System.out.println("Update on enrollments");
            client.updateTrials(ProtoUtils.getDroTrialList(response));
        }


    }

    private class ReaderThread implements Runnable{
        public void run() {
            while(!finished){
                try {
                    ProtocolProtobuff.Response response=ProtocolProtobuff.Response.parseDelimitedFrom(input);
                    System.out.println("response received "+response);
                    if (isUpdate(response)){
                        handleUpdate(response);
                    }else{

                        try {
                            qresponses.put(response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException | ServiceException e) {
                    System.out.println("Reading error "+e);

                }
            }
        }
    }
}
