module start.proiectjava {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.apache.logging.log4j;


    opens start.proiectjava to javafx.fxml;
    exports start.proiectjava;
    exports start.domain;
    exports start.service.dtos;
    exports start.service.interfaces;
    exports start.repository.interfaces;
}
