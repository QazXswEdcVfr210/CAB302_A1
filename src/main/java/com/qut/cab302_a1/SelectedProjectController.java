package com.qut.cab302_a1;

import com.qut.cab302_a1.models.Project;
import firebase.FirebaseDataStorage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class SelectedProjectController {

    static Project loadedProject;
    Image setPicture = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/qut/cab302_a1/pictures/forge.png")));

    @FXML
    private ImageView imageView;

    public static void setProject(Project project){
        loadedProject = project;
    }

    @FXML
    private Button returnButton;

    @FXML
    private Label projectName;

    @FXML
    private Label projectionLabel;

    @FXML
    private TextArea projectDescription;

    @FXML
    private HBox basePane;

   @FXML
   private ScrollPane mainScrollPane;

    public void initialize() {
        setText();
        getTipsList();
        mainScrollPane.setFitToWidth(true);

        imageView.setImage(setPicture);


        try{
            basePane.getStylesheets().add(getClass().getResource("stylesheets/selectedProjectPane.css").toExternalForm());
        }
        catch (Exception e){
            System.out.println("Stylesheet failed to load");
        }
    }

    public void setText(){
        projectName.setText(loadedProject.getName());
        projectDescription.setText(loadedProject.getDescription());


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
