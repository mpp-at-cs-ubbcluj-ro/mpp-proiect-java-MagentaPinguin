package client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import network.Proto.ServicesProtoProxy;
import services.IClientServices;
import network.RPC.ServicesRpcProxy;


import java.io.IOException;
import java.util.Properties;

public class Start extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        String serverIP= "127.0.0.1";
        int serverPort= 7777;

        try {
            Properties properties=new Properties();

            properties.load(Start.class.getResourceAsStream("clientProperties.properties"));
            serverIP = properties.getProperty("server.host", serverIP);
            serverPort=  Integer.parseInt(properties.getProperty("server.port"));
        } catch (IOException var13) {
            System.err.println("Cannot find chatclient.properties " + var13);
            return;
        }

        System.out.println("Using server IP " + serverIP);
        System.out.println("Using server port " + serverPort);

       // IClientServices service= new ServicesRpcProxy(serverIP,serverPort);
        IClientServices service= new ServicesProtoProxy(serverIP,serverPort);

        FXMLLoader loginLoader = new FXMLLoader(Start.class.getResource("LogIn.fxml"));
        Scene scene = new Scene(loginLoader.load());
        LogInController ctrl= loginLoader.getController();
        ctrl.setService(service);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args){
        launch();
    }
}