package network.Proto;

import model.Office;
import model.Participant;
import network.RPC.RequestType;
import services.IClientServices;
import services.IObserver;
import services.ServiceException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;


public class ClientProtoWorker implements Runnable, IObserver {

    private final IClientServices server;
    private final Socket connection;
    private InputStream input;
    private OutputStream output;
    private volatile boolean connected;

    public ClientProtoWorker(IClientServices server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try{
            output=connection.getOutputStream();
            output.flush();
            input=connection.getInputStream();
            connected=true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while(connected){
            try {
                ProtocolProtobuff.Request request= ProtocolProtobuff.Request.parseDelimitedFrom(input);
                ProtocolProtobuff.Response response=handleRequest(request);
                if (response!=null){
                    sendResponse(response);
                }
            } catch (IOException e) {
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
    private ProtocolProtobuff.Response handleRequest(ProtocolProtobuff.Request request){
        ProtocolProtobuff.Response response=null;
        //"LOGIN
        if (request.getType()== ProtocolProtobuff.Request.Type.Login){
            System.out.println("Login request ...");
            Office office=ProtoUtils.getOffice(request);
            try {
                var found =server.login(office, this);
                if(found!=null)
                    return ProtoUtils.createLoginResponse(found);

                return ProtocolProtobuff.Response.newBuilder().setType(ProtocolProtobuff.Response.Type.Error).setTxt("Not Found").build();
            } catch (ServiceException e) {
                connected=false;
                return ProtocolProtobuff.Response.newBuilder().setType(ProtocolProtobuff.Response.Type.Error).setTxt(e.getMessage()).build();
                //   return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        //"LOGOUT
        if (request.getType()== ProtocolProtobuff.Request.Type.Logout){
            System.out.println("Logout request");
            Office office= ProtoUtils.getOffice(request);

            try {
                server.logout(office);
                connected=false;
                return ProtocolProtobuff.Response.newBuilder().setType(ProtocolProtobuff.Response.Type.Login).setTxt("ok").build();

            } catch (ServiceException e) {
                return ProtocolProtobuff.Response.newBuilder().setType(ProtocolProtobuff.Response.Type.Error).setTxt(e.getMessage()).build();
            }
        }


        //"GET_PARTICIPANTS
        if (request.getType()== ProtocolProtobuff.Request.Type.GetParticipants){
            System.out.println("Participants request ...");
            try {

                var builder=ProtocolProtobuff.Response.newBuilder().setType(ProtocolProtobuff.Response.Type.GetParticipants);
                var data=server.getParticipants(); //Get data
                builder.addAllParticipants( ProtoUtils.toProtoParticipantList(data));
                return builder.build();

                //return new Response.Builder().type(ResponseType.OK).data(data).build();
            } catch (ServiceException e) {
                connected=false;
                return ProtocolProtobuff.Response.newBuilder().setType(ProtocolProtobuff.Response.Type.Error).setTxt("Eroare get participants").build();

            }
        }


        //"GET_TRIALS
        if (request.getType()== ProtocolProtobuff.Request.Type.GetTrials){
            System.out.println("Get yrials request ...");
            try {
                var data=server.getTrials();
                var protoData=ProtoUtils.toProtoDtoTrialList(data);
                return ProtocolProtobuff.Response.newBuilder().setType(ProtocolProtobuff.Response.Type.GetTrials).addAllDtoTrials(protoData).build();
                //return new Response.Builder().type(ResponseType.OK).data(data).build();
            } catch (ServiceException e) {
                connected=false;
                return ProtoUtils.createErrorResponse(e.getMessage());

            }
        }

        //"ADD_Participant
       if (request.getType()== ProtocolProtobuff.Request.Type.AddParticipant){
            System.out.println("ADD_Participant request");
            try {
                String name= request.getName();
                String cnp= request.getCnp();
                int age= request.getAge();
                server.addParticipant(name,cnp,age);
                return ProtoUtils.createOkResponse();
            } catch (ServiceException e) {
                connected=false;
                return ProtoUtils.createErrorResponse(e.getMessage());
            }
        }

        //"GetTrialsFor
        if ( request.getType()  == ProtocolProtobuff.Request.Type.GetEnrollmentsFor){
            System.out.println("Get enrollments for request");
            try {
                var list=server.GetEnrollmentsFor(request.getIdParticipant());
                return ProtoUtils.createGetEnrollmentsForResponse(list);

            } catch (ServiceException e) {
                connected=false;
                return ProtoUtils.createErrorResponse(e.getMessage());
            }
        }

        //"ADD_Enroll
        if (request.getType()== ProtocolProtobuff.Request.Type.AddEnroll){
            System.out.println("Enroll add request");
            try {

                server.addEnroll(request.getIdParticipant(),request.getIdTrial());
                return ProtoUtils.createOkResponse();
            } catch (ServiceException e) {
                connected=false;
                return ProtoUtils.createErrorResponse(e.getMessage());
            }
        }
        //"GET_EnrolledAt
        if (request.getType()== ProtocolProtobuff.Request.Type.GetEnrolledAt){
            System.out.println("Enrollment  at request");
            try {
                var list=server.getEnrolledAt(request.getIdTrial());
                return ProtoUtils.createEnrolledAtResponse(list);
            } catch (ServiceException e) {
                connected=false;
                return ProtoUtils.createErrorResponse(e.getMessage());
            }
        }

        return response;
    }

    private void sendResponse(ProtocolProtobuff.Response response) throws IOException{
        System.out.println("sending response "+response);
        response.writeDelimitedTo(output);
        output.flush();
    }


    @Override
    public void updateTrials() throws ServiceException {
        ProtocolProtobuff.Response resp=ProtoUtils.createAddEnrollResponse();
        System.out.println("Update trial received ");
        try {
            sendResponse(resp);
        } catch (IOException e) {
            throw new ServiceException("Sending error: "+e);
        }
    }

    @Override
    public void updateParticipants(Participant p) throws ServiceException {

        ProtocolProtobuff.Response resp=ProtoUtils.createAddParticipantResponse(p);
        System.out.println("Update participant received ");
        try {
            sendResponse(resp);
        } catch (IOException e) {
            throw new ServiceException("Sending error: "+e);
        }

    }
}
