module com.qut.cab302_a1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires functions.framework.api;
    requires java.logging;


    opens com.qut.cab302_a1 to javafx.fxml;
    exports com.qut.cab302_a1;
}