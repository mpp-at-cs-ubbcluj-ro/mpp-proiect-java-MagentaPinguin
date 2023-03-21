module start.proiectjava {
    requires javafx.controls;
    requires javafx.fxml;


    opens start.proiectjava to javafx.fxml;
    exports start.proiectjava;
}