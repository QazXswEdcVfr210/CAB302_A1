package com.qut.cab302_a1;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class SignupController {

    @FXML
    private VBox signupMain;

    @FXML
    private Hyperlink loginLink;

    @FXML
    private Button registerButton;


    @FXML
    public void initialize() {
        try{
            signupMain.getStylesheets().add(getClass().getResource("stylesheets/signupStylesheet.css").toExternalForm());
        }
        catch (Exception e){
            System.out.println("Stylesheet failed to load");
        }
    }

    @FXML
    public void signupButton(){

    }

    @FXML
    public void loginClick() throws IOException {
        Stage stage = (Stage) loginLink.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(SignupController.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), LoginApplication.HEIGHT, LoginApplication.WIDTH);
        stage.setTitle("Login");
        stage.setWidth(LoginApplication.WIDTH);
        stage.setHeight(LoginApplication.HEIGHT);
        stage.setScene(scene);
    }

}
