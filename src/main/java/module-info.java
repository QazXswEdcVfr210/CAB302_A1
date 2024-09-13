module com.qut.cab302_a1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;

    requires com.google.api.client;
    requires com.google.api.client.json.gson;
    requires com.google.gson;
    requires proto.google.common.protos;
    requires firebase.admin;

    requires jdk.xml.dom;
    requires java.sql;
    requires com.google.api.services.storage;

    opens com.qut.cab302_a1 to javafx.fxml;
    exports com.qut.cab302_a1;
}