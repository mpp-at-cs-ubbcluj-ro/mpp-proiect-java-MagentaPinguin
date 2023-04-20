package start.proiectjava;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import start.repository.*;
import start.service.*;

import java.io.FileReader;
import java.io.IOException;

import java.util.Properties;

public class Start extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        Properties properties=new Properties();
        properties.load(new FileReader("db.config"));

        Service service=new Service(
                 new ServiceOffice(new OfficeRepository(properties)),
                new ServiceParticipant(new ParticipantRepository(properties)),
                new ServiceTrial( new TrialRepository(properties)),
                new ServiceEnrollment( new EnrolledRepository(properties)));

        FXMLLoader fxmlLoader = new FXMLLoader(Start.class.getResource("LogIn.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        LogInController ctrl= fxmlLoader.getController();
        ctrl.setService(service);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args){
        launch();
    }
}