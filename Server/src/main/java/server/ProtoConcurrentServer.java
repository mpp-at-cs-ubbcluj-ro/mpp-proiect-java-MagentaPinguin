package server;

import network.Proto.ClientProtoWorker;
import services.IClientServices;

import java.net.Socket;


public class ProtoConcurrentServer extends AbstractConcurrentServer {
    private IClientServices chatServer;
    public ProtoConcurrentServer(int port, IClientServices chatServer) {
        super(port);
        this.chatServer = chatServer;
        System.out.println("ProtoSERVER START");
    }

    @Override
    protected Thread createWorker(Socket client) {
        // ChatClientRpcWorker worker=new ChatClientRpcWorker(chatServer, client);
        ClientProtoWorker worker=new ClientProtoWorker(chatServer, client);

        Thread tw=new Thread(worker);
        return tw;
    }

    @Override
    public void stop(){
        System.out.println("Stopping services ...");
    }

}
