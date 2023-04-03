package start.proiectjava;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import start.domain.Participant;
import start.service.dtos.DtoTrial;
import start.service.Service;
import start.service.ServiceException;

import java.util.stream.Collectors;

public class ViewOfficeController extends AbstractController {
    public Text show_msg;
    public TableView<DtoTrial> table_trial;
    public TableColumn<DtoTrial,String> col_nameTrial;
    public TableColumn<DtoTrial,String> col_maxTrial;
    public TableColumn<DtoTrial,String> col_minTrial;
    public TableColumn<DtoTrial,String> col_nr;
    private ObservableList<DtoTrial> model_trials= FXCollections.observableArrayList();
    //---------------------------------------------------------- Tabel office
    public Button add_participant;
    public TextField input_fullName;
    public TextField input_cnp;
    public TextField input_age;
    //----------------------------------------------------------

    public TableView<Participant> table_participant;
    public TableColumn<Participant,String> col_nameParticipant;
    public TableColumn<Participant,Integer> col_ageParticipant;
    private ObservableList<Participant> model_participant= FXCollections.observableArrayList();
    //----------------------------------------------------------
    public TextArea result_area;
    //----------------------------------------------------------

    void getModels(){

        try {
           model_participant.setAll(service.getParticipants());
           var c=service.getTrials();

            model_trials.setAll(c);
        } catch (ServiceException e) {
            popup(Type.WARNING,"ERROR",e.toString());
        }
    }

    @FXML
    void initialize(){
        col_ageParticipant.setCellValueFactory(new PropertyValueFactory<>("age"));
        col_nameParticipant.setCellValueFactory(new PropertyValueFactory<>("name"));
        //---------------------------------------------------------- Coloane tabel participanti

        col_nameTrial.setCellValueFactory(e->new SimpleStringProperty(e.getValue().getTrial().getName()));
        col_maxTrial.setCellValueFactory(e->new SimpleStringProperty(String.valueOf(e.getValue().getTrial().getMaxAge())));
        col_minTrial.setCellValueFactory(e->new SimpleStringProperty(String.valueOf(e.getValue().getTrial().getMinAge())));
        col_nr.setCellValueFactory(e->new SimpleStringProperty(String.valueOf(e.getValue().getNrOfEnrollments())));
        //---------------------------------------------------------- Coloane tabel trials
        table_participant.setItems(model_participant);
        table_trial.setItems(model_trials);

        table_trial.getSelectionModel().selectedItemProperty().addListener(e->modifyText());
    }

    private void modifyText() {
        try {
            var trial=table_trial.getSelectionModel().getSelectedItem();

            if(trial.getTrial()!=null){
                var resultString = service.getEnrolledAt(trial.getTrial()).
                        stream().
                        map(e -> "Name: " + e.getName() + " || Age: " + e.getAge()).collect(Collectors.joining("\n"));
                result_area.setText("Enrollments: \n" + resultString);
            }
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
    }

    public void setOfficeName(TextField username) {
        show_msg.setText("Bine ai revenit "+username.getText());
    }

    @Override
    void setService(Service s) {
        super.setService(s);
        getModels();
    }

    public void addParticipant(ActionEvent actionEvent) {
        try {
            service.addParticipant(input_fullName,input_cnp,input_age);
            getModels(); //! Update inplace
            input_fullName.clear();
            input_cnp.clear();
            input_age.clear();

        } catch (ServiceException e) {
            popup(Type.WARNING,"ERROR",e.toString());
        }

    }

    public void inscriere(ActionEvent actionEvent) {
        try{
           var p=table_participant.getSelectionModel().getSelectedItem();

           if(p == null)
               throw new ServiceException("Please select a participant!");

           var t=table_trial.getSelectionModel().getSelectedItem().getTrial();
           if(t == null)
               throw new ServiceException("Please select a trial!");

           if( service.getTrialsFor(p).size() ==2)
               throw new ServiceException("The participant has achieve the maximum nr. of enrollments!");
           service.addEnrollment(p,t);
           modifyText();

        }catch (ServiceException ex ){
           popup(Type.WARNING,"WARNING",ex.getMessage());
        }

    }
}
