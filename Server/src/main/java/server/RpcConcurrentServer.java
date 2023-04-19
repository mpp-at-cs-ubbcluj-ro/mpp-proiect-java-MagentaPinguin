package server;

import network.ClientRpcWorker;
import services.IClientServices;

import java.net.Socket;


public class RpcConcurrentServer extends AbstractConcurrentServer {
    private IClientServices chatServer;
    public RpcConcurrentServer(int port, IClientServices chatServer) {
        super(port);
        this.chatServer = chatServer;
        System.out.println("Chat- ChatRpcConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        // ChatClientRpcWorker worker=new ChatClientRpcWorker(chatServer, client);
        ClientRpcWorker worker=new ClientRpcWorker(chatServer, client);

        Thread tw=new Thread(worker);
        return tw;
    }

    @Override
    public void stop(){
        System.out.println("Stopping services ...");
    }

}
