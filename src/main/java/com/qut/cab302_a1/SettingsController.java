package com.qut.cab302_a1;


import com.qut.cab302_a1.models.Settings;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;


public class SettingsController {
   static int state = 0; //state 0 returns to login as default

    @FXML
    private VBox settingsMain;

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

}