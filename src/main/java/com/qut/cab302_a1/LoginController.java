package com.qut.cab302_a1;

import firebase.FirebaseRequestHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    public static final int MAIN_HEIGHT = 1000;
    public static final int MAIN_WIDTH = 600;

    @FXML
    private Label loginText;

    @FXML
    private Label incorrectPasswordLabel;

    @FXML
    private Hyperlink SignUpLink;

    @FXML
    private TextField loginField;

    @FXML
    private PasswordField passwordField;

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
    protected void onLoginButtonClick() throws Exception {

        // add logic for authentication and getting user's data from db here.
        // TODO: i dont know javafx but this is how you use the stuff i wrote to handle logins, if true then it was successful if false it was not.
        try {
            Boolean loginSuccess = FirebaseRequestHandler.TryLogin(loginField.getText(), passwordField.getText(), true);

            if (loginSuccess) {
                Stage stage = (Stage) LoginButton.getScene().getWindow();
                FXMLLoader fxmlLoader = new FXMLLoader(LoginApplication.class.getResource("project-view.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), MAIN_HEIGHT, MAIN_WIDTH);
                stage.setTitle("Project Partner");

                stage.setScene(scene);
            }
            else{
                incorrectPasswordLabel.setText("Invalid email or password");
                incorrectPasswordLabel.setVisible(true);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }




    }
}