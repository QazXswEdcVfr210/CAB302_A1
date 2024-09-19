package com.qut.cab302_a1;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class SignupController {

    @FXML
    private VBox signupMain;

    @FXML
    public void initialize() {
        try{
            signupMain.getStylesheets().add(getClass().getResource("stylesheets/signupStylesheet.css").toExternalForm());
        }
        catch (Exception e){
            System.out.println("Stylesheet failed to load");
        }
    }

}
