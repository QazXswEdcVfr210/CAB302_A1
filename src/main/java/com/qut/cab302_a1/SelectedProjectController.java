package com.qut.cab302_a1;

import firebase.FirebaseDataStorage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class SelectedProjectController {

    @FXML
    private Button returnButton;

    public void initialize() {

        getTipsList();
    }

    public void getTipsList(){
        //FirebaseDataStorage.
    }

    @FXML
    private void backAction(){
        try{
            Stage stage = (Stage) returnButton.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(LoginApplication.class.getResource("project-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), LoginController.MAIN_HEIGHT, LoginController.MAIN_WIDTH);
            stage.setTitle("Project Partner");
            stage.setWidth(LoginController.MAIN_WIDTH);
            stage.setHeight(LoginController.MAIN_HEIGHT);
            stage.setScene(scene);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

}
