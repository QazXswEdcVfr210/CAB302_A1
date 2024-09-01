package com.qut.cab302_a1;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    public static final int MAIN_HEIGHT = 1000;
    public static final int MAIN_WIDTH = 600;

    @FXML
    private Label loginText;

    @FXML
    private Hyperlink SignUpLink;

    @FXML
    protected void onSignup() throws IOException {
        Stage stage = (Stage) SignUpLink.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(LoginApplication.class.getResource("signup-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), LoginApplication.HEIGHT, LoginApplication.WIDTH);
        stage.setTitle("Sign Up");

        stage.setScene(scene);
    }

    @FXML
    private Button LoginButton;

    @FXML
    protected void onLoginButtonClick() throws IOException {
        // add logic for authentication and getting user's data from db here.

        Stage stage = (Stage) LoginButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(LoginApplication.class.getResource("project-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), MAIN_HEIGHT, MAIN_WIDTH);
        stage.setTitle("Project Partner");

        stage.setScene(scene);
    }
}