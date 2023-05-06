package client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import services.IClientServices;



public abstract class AbstractController {


    enum Type {
        CONFIRM,
        WARNING
    }

    abstract void setService(IClientServices s);


    protected void popup(Type type, String title, String msg) {
        Alert ex;
        switch (type) {
            case CONFIRM -> ex = new Alert(Alert.AlertType.CONFIRMATION);
            case WARNING -> ex = new Alert(Alert.AlertType.WARNING);
            default -> ex = new Alert(Alert.AlertType.INFORMATION);
        }
        ex.setTitle(title);
        ex.setContentText(msg);
        ex.show();
    }

    @FXML
    protected void exitScene(ActionEvent actionEvent) {
        Node crt = (Node) actionEvent.getSource();
        Stage crt_stage = (Stage) crt.getScene().getWindow();
        crt_stage.close();
    }


}
