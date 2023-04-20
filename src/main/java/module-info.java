module start.proiectjava {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.apache.logging.log4j;


    opens start.proiectjava to javafx.fxml;
    exports start.proiectjava;
    exports start.domain;
    exports start.service;
    exports start.service.dtos;
    exports start.repository;
    exports start.repository.interfaces;
}
