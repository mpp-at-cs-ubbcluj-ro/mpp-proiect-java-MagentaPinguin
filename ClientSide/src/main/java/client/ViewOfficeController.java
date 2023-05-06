package client;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import model.*;
import services.IClientServices;
import services.IObserver;
import services.ServiceException;


import java.util.List;
import java.util.stream.Collectors;

public class ViewOfficeController extends AbstractController implements IObserver {
    private IClientServices service;
    private Office user;
    public Text show_msg;
    public TableView<DtoTrial> table_trial=new TableView<>();
    public TableColumn<DtoTrial,String> col_nameTrial=new TableColumn<>();
    public TableColumn<DtoTrial,String> col_maxTrial=new TableColumn<>();
    public TableColumn<DtoTrial,String> col_minTrial=new TableColumn<>();
    public TableColumn<DtoTrial,String> col_nr=new TableColumn<>();
    private ObservableList<DtoTrial> model_trials= FXCollections.observableArrayList();
    //---------------------------------------------------------- Tabel office
    public Button add_participant;
    public TextField input_fullName;
    public TextField input_cnp;
    public TextField input_age;
    //----------------------------------------------------------

    public TableView<Participant> table_participant=new TableView<>();
    public TableColumn<Participant,String> col_nameParticipant=new TableColumn<>();
    public TableColumn<Participant,Integer> col_ageParticipant=new TableColumn<>();
    private ObservableList<Participant> model_participant= FXCollections.observableArrayList();
    //----------------------------------------------------------
    public TextArea result_area;
    //----------------------------------------------------------


    @Override
    public void updateTrials(List<DtoTrial> newList) {
        Platform.runLater(() -> {
                model_trials.setAll(newList);
            }
        );
    }
    @Override
    public void updateParticipants( Participant p) {
    Platform.runLater(() -> {
        model_participant.add(p);}
        );
    }

    @FXML
    void initialize(){
        System.out.println("HELLOW!");
        col_nameParticipant.setCellValueFactory(new PropertyValueFactory<>("name"));

        col_ageParticipant.setCellValueFactory(new PropertyValueFactory<>("age"));

        table_participant.setItems(model_participant);
        //---------------------------------------------------------- Coloane tabel participanti

        col_nameTrial.setCellValueFactory(e->new SimpleStringProperty(e.getValue().getTrial().getName()));

        col_maxTrial.setCellValueFactory(e->new SimpleStringProperty(String.valueOf(e.getValue().getTrial().getMaxAge())));

        col_minTrial.setCellValueFactory(e->new SimpleStringProperty(String.valueOf(e.getValue().getTrial().getMinAge())));

        col_nr.setCellValueFactory(e->new SimpleStringProperty(String.valueOf(e.getValue().getNrOfEnrollments())));

        table_trial.setItems(model_trials);

        //---------------------------------------------------------- Coloane tabel trials

        table_trial.getSelectionModel().selectedItemProperty().addListener(e->modifyText());
    }

    private void modifyText() {
        try {
            var trial=table_trial.getSelectionModel().getSelectedItem();

            if(trial!=null){
                var resultString = service.getEnrolledAt(trial.getTrial().getId()).
                        stream().
                        map(e -> "Name: " + e.getName() + " || Age: " + e.getAge()).collect(Collectors.joining("\n"));
                result_area.setText("Enrollments: \n" + resultString);
            }
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
    }

    public void setUser(Office office) {
        user=office;
        show_msg.setText("Bine ai revenit "+office.getUsername());

    }
    @Override
    void setService(IClientServices s) {
        this.service=s;
       try {
            model_participant.setAll(service.getParticipants());
            model_trials.setAll(service.getTrials());
        } catch (Exception e) {
            popup(Type.WARNING,"Error",e.getMessage());
        } // Get initial values
    }

    public void addParticipant(ActionEvent actionEvent) {
        try {
            service.addParticipant(input_fullName.getText(),input_cnp.getText(),Integer.parseInt(input_age.getText()));
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
           var t=table_trial.getSelectionModel().getSelectedItem();

           if(t == null)
               throw new ServiceException("Please select a trial!");

           var x=service.GetEnrollmentsFor(p.getId());

           if(x.contains(t.getTrial()))
                throw new ServiceException("Already an attende!");

            if(x.size()==2)
               throw new ServiceException("The participant has achieve the maximum nr. of enrollments!");
            service.addEnroll(p.getId(),t.getTrial().getId());

        }catch (ServiceException ex ){
           popup(Type.WARNING,"WARNING",ex.getMessage());
        }
    }

    public void logout(ActionEvent actionEvent) {
        try {

            service.logout(user);
            exitScene(actionEvent);

        } catch (ServiceException e) {
            popup(Type.WARNING,"Error",e.toString());
        }
    }


}
