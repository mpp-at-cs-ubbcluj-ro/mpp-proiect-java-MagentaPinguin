package client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Office;
import services.IClientServices;
import services.ServiceException;

import java.io.IOException;

public class LogInController extends AbstractController{

    private IClientServices service;
    public TextField input_username;
    public TextField input_passwd;

    @FXML
    void initialize(){

    }

    public void login(ActionEvent mouseEvent) {
        try {
            var found =this.service.login(new Office(input_username.getText(),input_passwd.getText()), new ViewOfficeController());
            if(found!=null){
                FXMLLoader fxmlLoader = new FXMLLoader(Start.class.getResource("ViewOffice.fxml"));
                Scene scene = new Scene(fxmlLoader.load()) ;
                ViewOfficeController ctrl=fxmlLoader.getController();
                ctrl.setService(service);
                ctrl.setUser(found);

                Stage stage=new Stage();
                stage.setScene(scene);
                stage.setResizable(false);
                stage.show();
                this.exitScene(mouseEvent);
            }
        } catch (ServiceException | IOException ex) {
            popup(Type.WARNING,"Error",ex.getMessage());
        }
    }

    @Override
    void setService(IClientServices s) {
        this.service=s;
    }
}