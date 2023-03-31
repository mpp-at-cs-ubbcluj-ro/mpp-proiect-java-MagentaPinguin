package start.proiectjava;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.util.Pair;
import start.domain.Participant;
import start.domain.Trial;
import start.service.Service;
import start.service.ServiceException;

import java.net.ServerSocket;
import java.util.stream.Collectors;

public class ViewOfficeController extends AbstractController {
    public Text show_msg;
    public TableView<Pair<Trial,Long>> table_trial;
    public TableColumn<Pair<Trial,Long>,String> col_nameTrial;
    public TableColumn<Pair<Trial,Long>,String> col_maxTrial;
    public TableColumn<Pair<Trial,Long>,String> col_minTrial;
    public TableColumn<Pair<Trial,Long>,String> col_nr;

    private ObservableList<Pair<Trial,Long>> model_trials= FXCollections.observableArrayList();
    //---------------------------------------------------------- Tabel office
    public Button add_participant;
    public TextField input_fullName;
    public TextField input_cnp;
    public TextField input_age;
    //----------------------------------------------------------
    public Button add_trial;
    public TextField input_trialName;
    public TextField input_maxAge;
    public TextField input_minAge;
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
           var c=service.getTrials()
                   .stream()
                   .map(e-> {
                       try {
                           return new Pair<Trial,Long>(e,service.getEnrolledAt(e).stream().count());
                       } catch (ServiceException ex) {
                           throw new RuntimeException(ex);
                       }
                   }).toList();

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

        col_nameTrial.setCellValueFactory(e->new SimpleStringProperty(e.getValue().getKey().getName()));
        col_maxTrial.setCellValueFactory(e->new SimpleStringProperty(String.valueOf(e.getValue().getKey().getMaxAge())));
        col_minTrial.setCellValueFactory(e->new SimpleStringProperty(String.valueOf(e.getValue().getKey().getMinAge())));
        col_nr.setCellValueFactory(e->new SimpleStringProperty(e.getValue().getValue().toString()));
        //---------------------------------------------------------- Coloane tabel trials
        table_participant.setItems(model_participant);
        table_trial.setItems(model_trials);

        table_trial.getSelectionModel().selectedItemProperty().addListener(e->modifyText());
        table_participant.getSelectionModel().selectedItemProperty().addListener(e->modifyText());

    }

    private void modifyText() {
//        try {
//            var string = "";
//            if (table_trial.getSelectionModel().isEmpty()){
//                var p=table_participant.getSelectionModel().getSelectedItem();
//                string = service.getTrialsFor(p)
//                        .stream()
//                        .map(Trial::getName)
//                        .collect(Collectors.joining("\n"));
//                if(string.isEmpty())
//                    string="Nimic";
//                string = p.getName()+" este inscris/a la :\n" +string;
//            }
//            if (table_participant.getSelectionModel().isEmpty()){
//                var t=table_trial.getSelectionModel().getSelectedItem();
//                string = service.getEnrolledAt(t)
//                        .stream()
//                        .map(Participant::getName)
//                        .collect(Collectors.joining("\n"));
//                if(string.isEmpty())
//                    string="Nimeni";
//                string = "La "+t.getName()+" s-au inscris :\n" +string;
//            }
//            result_area.setText(string);
//        } catch (ServiceException e) {
//            throw new RuntimeException(e);
//        }
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


    public void addTrial(ActionEvent actionEvent) {
        try {
            service.addTrial(  input_trialName, input_minAge, input_maxAge);
        getModels(); //! Update inplace
        input_trialName.clear();
        input_minAge.clear();
        input_maxAge.clear();
        }
        catch (ServiceException e) {
            popup(Type.WARNING,"ERROR",e.toString());
        }
    }

    public void inscriere(ActionEvent actionEvent) {
        try{
           var p=table_participant.getSelectionModel().getSelectedItem();
           if(p == null)
               throw new ServiceException("Please select a participant!");

           var t=table_trial.getSelectionModel().getSelectedItem().getKey();
           if(t == null)
               throw new ServiceException("Please select a trial!");

           if( service.getTrialsFor(p).size() ==2)
               throw new ServiceException("The participant has achieve the maximum nr. of enrollments!");

           service.addEnrollment(p,t);


        }catch (ServiceException ex ){
            System.out.println("x");
        }

    }
}
