package com.qut.cab302_a1;


import com.qut.cab302_a1.models.Settings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;


public class SettingsController {
   static int state = 0; //state 0 returns to login as default

    @FXML
    private VBox settingsMain;

   @FXML
   private Button backButton;

   @FXML
   private Button buttonLogout;

    public static void setState(Settings page){
        switch(page){
            case LOGIN:
                state = 0;
                break;

            case REGISTER:
                state = 1;
                break;

            case LIST:
                state = 2;
                break;

            case PROJECT:
                state = 3;
                break;

            default:
                state = 0;
                break;
        }
    }

    @FXML
    public void initialize() {
        try{
            settingsMain.getStylesheets().add(getClass().getResource("stylesheets/settingsStylesheet.css").toExternalForm());
        }
        catch (Exception e){
            System.out.println("Stylesheet failed to load");
        }
    }

    @FXML
    public void logoutAction(){
        try{
            Stage stage = (Stage) buttonLogout.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(LoginApplication.class.getResource("login-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), LoginApplication.HEIGHT, LoginApplication.WIDTH);
            stage.setTitle("Project Partner");
            stage.setWidth(LoginApplication.WIDTH);
            stage.setHeight(LoginApplication.HEIGHT);
            stage.setScene(scene);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }


}