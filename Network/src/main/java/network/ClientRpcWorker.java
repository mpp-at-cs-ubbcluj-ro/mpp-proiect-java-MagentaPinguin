package network;

import model.Office;
import model.Participant;
import model.Trial;
import services.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;


public class ClientRpcWorker implements Runnable, IObserver {

    private IClientServices server;
    private Socket connection;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;

    public ClientRpcWorker(IClientServices server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try{
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            connected=true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while(connected){
            try {
                Object request=input.readObject();
                Response response=handleRequest((Request)request);
                if (response!=null){
                    sendResponse(response);
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Error "+e);
        }
    }

    private static final Response okResponse=new Response.Builder().type(ResponseType.OK).build();
    private static final Response errorResponse=new Response.Builder().type(ResponseType.ERROR).build();

    private Response handleRequest(Request request){
        Response response=null;
        //"LOGIN
        if (request.type()== RequestType.LOGIN){
            System.out.println("Login request ..."+request.type());
            Office office=(Office)request.data();
            try {
                var found =server.login(office, this);
                if(found!=null)
                    return new Response.Builder().type(ResponseType.OK).data(found).build();
                return errorResponse;

            } catch (ServiceException e) {
                connected=false;
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        //"LOGOUT
        if (request.type()== RequestType.LOGOUT){
            System.out.println("Logout request");
            Office user= (Office) request.data();
            try {
                server.logout(user, this);
                connected=false;
                return okResponse;

            } catch (ServiceException e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }

        //"GET_PARTICIPANTS
        if (request.type()== RequestType.GET_PARTICIPANTS){
            System.out.println("Participants request ...");

            try {
                var data=server.getParticipants();
                return new Response.Builder().type(ResponseType.OK).data(data).build();
            } catch (ServiceException e) {
                connected=false;
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();

            }
        }

        //"GET_TRIALS
        if (request.type()== RequestType.GET_TRIALS){
            System.out.println("Trials request ...");
            try {
                var data=server.getTrials();
                return new Response.Builder().type(ResponseType.OK).data(data).build();
            } catch (ServiceException e) {
                connected=false;
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();

            }
        }

        //"ADD_Participant
       if (request.type()== RequestType.ADD_PARTICIPANT){
            System.out.println("ADD_Participant request");
            try {
                server.addParticipant((Participant) request.data());
                return okResponse;
            } catch (ServiceException e) {
                connected=false;
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        //"GetTrialsNr
        if (request.type()== RequestType.GET_TrialsFor){
            System.out.println("ADD_Participant request");
            try {
               return new Response.Builder().type(ResponseType.OK)
                       .data(server.getTrialsFor((Participant) request.data())).build();
            } catch (ServiceException e) {
                connected=false;
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }

        //"ADD_Enroll
        if (request.type()== RequestType.ADD_ENROLL){
            System.out.println("Enroll add request");
            try {
                var x = (List)request.data();
                server.addEnroll((Participant) x.get(0),(Trial) x.get(1));
                return okResponse;
            } catch (ServiceException e) {
                connected=false;
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        //"GET_EnrolledAt
        if (request.type()== RequestType.GET_EnrolledAt){
            System.out.println("Enrollment  at request");
            try {
                var t = (Trial) request.data();
                var list=server.getEnrolledAt(t);
                return new Response.Builder().type(ResponseType.OK).data(list).build();
            } catch (ServiceException e) {
                connected=false;
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }

        return response;
    }

    private void sendResponse(Response response) throws IOException{
        System.out.println("sending response "+response);
        output.writeObject(response);
        output.flush();
    }


 /*   @Override
    public void updateTrials() {

    }*/

    @Override
    public void updateParticipants(Participant p) throws ServiceException {
        Response resp=new Response.Builder().type(ResponseType.PARTICIPANT_ADDED).data(p).build();
        System.out.println("Update received ");
        try {
            sendResponse(resp);
        } catch (IOException e) {
            throw new ServiceException("Sending error: "+e);
        }

    }
}
