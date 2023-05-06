package network.Proto;
import model.*;
import network.RPC.Response;

import java.util.List;

public class ProtoUtils {

    public static ProtocolProtobuff.Request creaateLoginRequest(Office of){
        ProtocolProtobuff.Office office= ProtocolProtobuff.Office.
                newBuilder().
                setUsername(of.getUsername()).
                setPassword(of.getPassword()).build();
        //Create protoOffice from Office
        ProtocolProtobuff.Request loginReq= ProtocolProtobuff.Request.
                newBuilder().
                setType(ProtocolProtobuff.Request.Type.Login).
                setOffice(office)
                .build();
        //Create protoRequest.
        return loginReq;
    }

    public static ProtocolProtobuff.Request createLogoutRequest(Office of){
        System.out.println("PROTO\n"+of);
        ProtocolProtobuff.Office office= ProtocolProtobuff.Office.
                newBuilder().
                setId(of.getId().intValue()).
                setUsername(of.getUsername()).
                setPassword(of.getPassword()).build();
        //Create protoOffice from Office
        ProtocolProtobuff.Request logout= ProtocolProtobuff.Request.
                newBuilder().
                setType(ProtocolProtobuff.Request.Type.Logout).
                setOffice(office)
                .build();
        //Create protoRequest.
        return logout;
    }
    //REQUESTS
    public static ProtocolProtobuff.Response createLoginResponse(Office of){
        ProtocolProtobuff.Office office= ProtocolProtobuff.Office.
                newBuilder().
                setId(of.getId().intValue()).
                setUsername(of.getUsername()).
                setPassword(of.getPassword()).build();
        //Create protoOffice from Office
        ProtocolProtobuff.Response loginResponse= ProtocolProtobuff.Response.
                newBuilder().
                setType(ProtocolProtobuff.Response.Type.Login).
                setOffice(office)
                .build();
        //Create protoRequest.
        return loginResponse;
    }
    public static ProtocolProtobuff.Response createLogoutResponse(Office of) {
        ProtocolProtobuff.Office office = ProtocolProtobuff.Office.
                newBuilder().
                setId(of.getId().intValue()).
                setUsername(of.getUsername()).
                setPassword(of.getPassword()).build();
        //Create protoOffice from Office
        ProtocolProtobuff.Response logoutResponse = ProtocolProtobuff.Response.
                newBuilder().
                setType(ProtocolProtobuff.Response.Type.Logout).
                setOffice(office)
                .build();
        //Create protoRequest.
        return logoutResponse;
    }

    public static ProtocolProtobuff.Response createOkResponse() {

        ProtocolProtobuff.Response okResponse = ProtocolProtobuff.Response.
                newBuilder().
                setType(ProtocolProtobuff.Response.Type.Ok).
                build();
        //Create protoRequest.
        return okResponse;
    }
    public static ProtocolProtobuff.Response createErrorResponse(String txt ) {

        ProtocolProtobuff.Response error = ProtocolProtobuff.Response.
                newBuilder().
                setType(ProtocolProtobuff.Response.Type.Error).
                build();
        //Create protoRequest.
        return error;
    }
    //RESPONSES

    static Office getOffice(ProtocolProtobuff.Response response){
        Office off=new Office((long)response.getOffice().getId(),response.getOffice().getUsername(),response.getOffice().getPassword());
        return off;
    }
    static Office getOffice(ProtocolProtobuff.Request request){
        Office off=new Office((long)request.getOffice().getId(),request.getOffice().getUsername(),request.getOffice().getPassword());
        System.out.println(off);
        return off;
    }

    static List<Participant> getParticipantList(ProtocolProtobuff.Response response){
        System.out.println("\n\n "+response.getParticipantsList());
        return response.getParticipantsList().stream()
                .map(e-> new Participant((long) e.getId(),e.getName(), e.getCnp(), e.getAge())).toList();

    }

    static List<ProtocolProtobuff.Participant> toProtoParticipantList(List<Participant>  data){
        return data.stream().map(p ->
                ProtocolProtobuff.Participant.newBuilder().
                        setId(p.getId().intValue())
                        .setName(p.getName())
                        .setCnp(p.getCnp())
                        .setAge(p.getAge())
                        .build())
                        .toList();
    }


    static List<DtoTrial> getDroTrialList(ProtocolProtobuff.Response response){

        return response.getDtoTrialsList().stream()
                .map(t -> {
                    return new DtoTrial(
                            new Trial(
                                    (long)t.getTrial().getId(),
                                    t.getTrial().getName(),
                                    t.getTrial().getMinAge(),
                                    t.getTrial().getMaxAge()
                                    )
                            ,t.getNrOfEnrollments());
                }).toList();

    }

    static List<ProtocolProtobuff.DtoTrial> toProtoDtoTrialList(List<DtoTrial>  data){
        return data.stream().map(t->
                ProtocolProtobuff.DtoTrial.newBuilder()
                        .setTrial(
                                ProtocolProtobuff.Trial.newBuilder()
                                        .setId((int)t.getId())
                                        .setMinAge(t.getMinAge())
                                        .setMaxAge(t.getMaxAge())
                                        .setName(t.getName())
                                        .build())
                        .setNrOfEnrollments(t.getNrOfEnrollments()).build()).toList();

    }
    static List<ProtocolProtobuff.Trial> toProtoTrialList(List<Trial>  data){
        return data.stream().map(t->
                ProtocolProtobuff.Trial.newBuilder()
                                        .setId(t.getId().intValue())
                                        .setMinAge(t.getMinAge())
                                        .setMaxAge(t.getMaxAge())
                                        .setName(t.getName())
                                        .build()).toList();


    }

    public static ProtocolProtobuff.Request createAddParticipantRequest(String name, String cnp, int age) {
        ProtocolProtobuff.Request req=ProtocolProtobuff.Request
                .newBuilder()
                .setType(ProtocolProtobuff.Request.Type.AddParticipant)
                .setName(name).setCnp(cnp).setAge(age).build();

        return req;

    }
    public static ProtocolProtobuff.Response createAddParticipantResponse(Participant p) {
        return ProtocolProtobuff.Response.newBuilder()
                .setType(ProtocolProtobuff.Response.Type.AddParticipant)
                .setUpdateParticipant(ProtocolProtobuff.Participant
                        .newBuilder()
                        .setId(p.getId().intValue())
                        .setName(p.getName())
                        .setCnp(p.getCnp())
                        .setAge(p.getAge()).build()).build();
    }

    public static Participant getParticipant(ProtocolProtobuff.Response response) {
        var x=response.getUpdateParticipant();
        return new Participant((long)x.getId(),x.getName(),x.getCnp(),x.getAge());
    }


    public static ProtocolProtobuff.Response createEnrolledAtResponse(List<Participant> list) {
        System.out.println(toProtoParticipantList(list));
        return ProtocolProtobuff.Response.newBuilder()
                .addAllParticipants(toProtoParticipantList(list))
                .setType(ProtocolProtobuff.Response.Type.GetEnrolledAt).build();
    }

    public static ProtocolProtobuff.Request createGetEnrolledAtRequest(long idTrial) {
        return ProtocolProtobuff.Request.newBuilder().setType(ProtocolProtobuff.Request.Type.GetEnrolledAt).setIdTrial((int)idTrial).build();
    }


    public static List<Trial> getTrialList(ProtocolProtobuff.Response response) {
        return response.getTrialsList().stream()
                .map(t -> {
                    return  new Trial(
                                    (long)t.getId(),
                                    t.getName(),
                                    t.getMinAge(),
                                    t.getMaxAge()
                            );
                }).toList();

    }

    public static ProtocolProtobuff.Request createGetEnrollmentsForRequest(long idP) {
            return ProtocolProtobuff.Request
                    .newBuilder()
                    .setType(ProtocolProtobuff.Request.Type.GetEnrollmentsFor)
                    .setIdParticipant((int)idP)
                    .build();

    }

    public static ProtocolProtobuff.Response createGetEnrollmentsForResponse(List<Trial> list) {
        return ProtocolProtobuff.Response.newBuilder()
                .setType(ProtocolProtobuff.Response.Type.GetEnrolledAt)
                .addAllTrials(toProtoTrialList(list)).build();
    }

    public static ProtocolProtobuff.Request createAddEnroll(long p, long t) {
        return ProtocolProtobuff.Request.newBuilder()
                .setType(ProtocolProtobuff.Request.Type.AddEnroll)
                .setIdTrial((int)t)
                .setIdParticipant((int)p)
                .build();
    }

    public static ProtocolProtobuff.Response createAddEnrollResponse(List<DtoTrial> trials) {
        return ProtocolProtobuff.Response .newBuilder()
                .setType(ProtocolProtobuff.Response.Type.AddEnroll)
                .addAllDtoTrials(toProtoDtoTrialList(trials))
                .build();
    }


    //Getters

}
