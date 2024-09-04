module com.qut.cab302_a1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;
    requires com.google.api.client;
    requires com.google.api.client.json.gson;

    opens com.qut.cab302_a1 to javafx.fxml;
    exports com.qut.cab302_a1;
}