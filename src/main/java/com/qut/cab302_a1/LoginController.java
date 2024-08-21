package com.qut.cab302_a1;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
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
    protected void onLoginButtonClick() {
        loginText.setText("Login");
    }
}