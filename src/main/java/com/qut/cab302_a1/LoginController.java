package com.qut.cab302_a1;

import com.qut.cab302_a1.models.Settings;
import firebase.FirebaseRequestHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    public static final int MAIN_WIDTH = 1000;
    public static final int MAIN_HEIGHT = 600;

    private String user;

    @FXML
    private Button settingsButton;

    @FXML
    private VBox loginMain;

    @FXML
    public void initialize() {
        try{
            loginMain.getStylesheets().add(getClass().getResource("stylesheets/loginStylesheet.css").toExternalForm());
        }
        catch (Exception e){
            System.out.println("Stylesheet failed to load");
        }
    }

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

    /**
     * Link that takes user to the signup page.
     *
     * @throws IOException
     */
    @FXML
    protected void onSignup() throws IOException {
        Stage stage = (Stage) SignUpLink.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(LoginApplication.class.getResource("signup-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), LoginApplication.HEIGHT, LoginApplication.WIDTH);
        stage.setTitle("Sign Up");
        stage.setWidth(LoginApplication.WIDTH);
        stage.setHeight(LoginApplication.HEIGHT);
        stage.setScene(scene);
    }

    @FXML
    private Button LoginButton;


    /**
     * Gets email and password parameters and sends them to db. Db validates user and returns a boolean.
     * If boolean is true, Takes user to the main projects page and shows them their projects.
     * @throws Exception
     */
    @FXML
    protected void onLoginButtonClick() throws Exception {


        try {
            Boolean loginSuccess = FirebaseRequestHandler.TryLogin(loginField.getText(), passwordField.getText(), false);

            // If login is successful change scene
            if (loginSuccess) {
                user = loginField.getText();
                Stage stage = (Stage) LoginButton.getScene().getWindow();
                FXMLLoader fxmlLoader = new FXMLLoader(LoginApplication.class.getResource("project-view.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), MAIN_WIDTH, MAIN_HEIGHT);
                stage.setTitle("Project Partner");
                stage.setWidth(MAIN_WIDTH);
                stage.setHeight(MAIN_HEIGHT);
                stage.setScene(scene);
            }
            //else update label
            else{
                incorrectPasswordLabel.setText("Invalid email or password");
                incorrectPasswordLabel.setVisible(true);
            }
        }
        catch (Exception e) {

            e.printStackTrace();
        }
    }

    @FXML
    public void settingsClick() throws IOException {
        Stage stage = (Stage) settingsButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(LoginApplication.class.getResource("settings-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), LoginApplication.HEIGHT, LoginApplication.WIDTH);
        stage.setTitle("Settings");
        stage.setWidth(LoginApplication.WIDTH);
        stage.setHeight(LoginApplication.HEIGHT);
        SettingsController.setState(Settings.LOGIN);
        stage.setScene(scene);
    }

    @FXML
    protected void google(){
        incorrectPasswordLabel.setText("Feature coming soon");
        incorrectPasswordLabel.setVisible(true);
    }

    @FXML
    protected void apple(){
        incorrectPasswordLabel.setText("Feature coming soon");
        incorrectPasswordLabel.setVisible(true);
    }
}