package com.qut.cab302_a1;

import com.qut.cab302_a1.models.Project;
import com.qut.cab302_a1.models.ProjectStep;
import firebase.FirebaseDataStorage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SelectedProjectController {

    static Project loadedProject;
    public static List<ProjectStep> steps = new ArrayList<>();
    Image setPicture = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/qut/cab302_a1/pictures/forge.png")));

    @FXML
    private ImageView imageView;

    @FXML
    private StackPane progressBarPane;

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

        setLoadingBar();
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

    public void setLoadingBar(){
        VBox progressBar = new VBox(20);
        //top row
        HBox progressBox = new HBox(20);
        progressBox.setSpacing(185);

        final int MAX_RANGE = 750;
        final int MAX_WIDTH = 5;
        int progressRange = ProjectController.calculateProgress(MAX_RANGE, 10, 5);

        HBox.setHgrow(progressBarPane, Priority.ALWAYS); // figure this out later. Meant to be growth between label and progressPane.
        StackPane progressPane = new StackPane();
        progressPane.setAlignment(Pos.CENTER);
        Rectangle backBar = new Rectangle(MAX_RANGE, MAX_WIDTH+0.2);

        backBar.setArcWidth(10);
        backBar.setArcHeight(2);
        backBar.setFill(Color.GRAY);

        //progress bar gradient should range from blue to red.
        Rectangle progressionBar = new Rectangle(progressRange, MAX_WIDTH);
        Color colorPicker = ProjectController.pickColor(MAX_RANGE, progressRange);

        progressionBar.setFill(colorPicker);
        backBar.setArcWidth(10);
        backBar.setArcHeight(2);

        progressPane.setAlignment(Pos.CENTER_LEFT);
        progressPane.getChildren().addAll(backBar, progressionBar);

        progressBar.getChildren().addAll(progressBox, progressPane);
        progressBarPane.getChildren().addAll(progressBar);

    }

    public void loadSteps(){


        if (loadedProject.getProjectSteps() != null || loadedProject.getProjectSteps().size() > 0){
           steps = loadedProject.getProjectSteps();

           for (ProjectStep step : steps){
               createStep(step);
           }
        }
    }

    public void createStep(ProjectStep step){

    }

}
