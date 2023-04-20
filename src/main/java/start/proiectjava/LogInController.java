package start.proiectjava;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import javafx.scene.control.TextField;
import javafx.stage.Stage;

import start.service.ServiceException;

import java.io.IOException;

public class LogInController extends AbstractController{


    public TextField input_username;
    public TextField input_passwd;

    @FXML
    void initialize(){

    }

    public void login(ActionEvent mouseEvent) {
        try {
            if(this.service.login(input_username.getText(),input_passwd.getText())){
                FXMLLoader fxmlLoader = new FXMLLoader(Start.class.getResource("ViewOffice.fxml"));
                Scene scene = new Scene(fxmlLoader.load()) ;
                ViewOfficeController ctrl=fxmlLoader.getController();
                ctrl.setService(service);
                ctrl.setOfficeName(input_username);

                Stage stage=new Stage();
                stage.setScene(scene);
                stage.setResizable(false);
                stage.show();
                this.exitScene(mouseEvent);
                return;
            }
            popup(Type.WARNING,"Login faild!","Login attempt faild.");
        } catch (ServiceException | IOException e) {
            throw new RuntimeException(e);
        }
    }

}