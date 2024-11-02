package com.qut.cab302_a1;

import com.qut.cab302_a1.models.Settings;
import firebase.FirebaseRequestHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;

public class SignupController {

    private String user;

    @FXML
    private Label failLabel;

    @FXML
    private TextField registerField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmField;

    @FXML
    private VBox signupMain;

    @FXML
    private Hyperlink loginLink;

    @FXML
    private Button settingsButton;

    @FXML
    private Button registerButton;


    @FXML
    public void initialize() {
        try {
            signupMain.getStylesheets().add(getClass().getResource("stylesheets/signupStylesheet.css").toExternalForm());
        } catch (Exception e) {
            System.out.println("Stylesheet failed to load");
        }
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

    @FXML
    public void settingsClick() throws IOException {
        Stage stage = (Stage) settingsButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(SettingsController.class.getResource("settings-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), LoginApplication.HEIGHT, LoginApplication.WIDTH);
        stage.setTitle("Settings");
        stage.setWidth(LoginApplication.WIDTH);
        stage.setHeight(LoginApplication.HEIGHT);
        SettingsController.setState(Settings.REGISTER);
        stage.setScene(scene);
    }


    /**
     * Gets email and password parameters and sends them to db. Db validates user and returns a boolean.
     * If boolean is true, Takes user to the main projects page and shows them their projects.
     *
     * @throws Exception
     */
    @FXML
    protected void signupButton() throws Exception {

        if (passwordField.getText().equals(confirmField.getText())) {
            try {
                Boolean registerSuccess = FirebaseRequestHandler.TrySignup(registerField.getText(), passwordField.getText(), registerField.getText(), false);

                // If login is successful change scene
                if (registerSuccess) {
                    user = registerField.getText();
                    Stage stage = (Stage) registerButton.getScene().getWindow();
                    FXMLLoader fxmlLoader = new FXMLLoader(LoginApplication.class.getResource("project-view.fxml"));
                    Scene scene = new Scene(fxmlLoader.load(), LoginController.MAIN_WIDTH, LoginController.MAIN_HEIGHT);
                    stage.setTitle("Project Partner");
                    stage.setWidth(LoginController.MAIN_WIDTH);
                    stage.setHeight(LoginController.MAIN_HEIGHT);
                    stage.setScene(scene);
                }
                //else update label
                else {
                    failLabel.setText("Invalid email or password");
                    failLabel.setVisible(true);
                }
            } catch (Exception e) {

                e.printStackTrace();
            }
        } else {
            failLabel.setText("Passwords do not match");
            failLabel.setVisible(true);
        }
    }

    @FXML
    protected void google() {
        failLabel.setText("Feature coming soon");
        failLabel.setVisible(true);
    }

    @FXML
    protected void apple() {
        failLabel.setText("Feature coming soon");
        failLabel.setVisible(true);
    }
}