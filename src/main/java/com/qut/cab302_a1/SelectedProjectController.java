package com.qut.cab302_a1;

import com.qut.cab302_a1.models.Project;
import com.qut.cab302_a1.models.ProjectStep;
import firebase.FirebaseDataStorage;
import firebase.FirebaseRequestHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
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

    int incompleteStepCount;
    int completedSteps = 0;
    int totalStepCount = 0;
    static Project loadedProject;
    public static List<ProjectStep> steps = new ArrayList<>();
    ArrayList<HBox> stepBoxes = new ArrayList<>();
    ArrayList<ProjectStep> newSteps = new ArrayList<>();
    Image setPicture = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/qut/cab302_a1/pictures/forge.png")));

    @FXML
    private ImageView imageView;

    @FXML
    private StackPane progressBarPane;

    @FXML
    private VBox stepsPane;

    public static void setProject(Project project){
        loadedProject = project;
    }

    @FXML
    private Button returnButton;

    @FXML
    private Label projectName;

    @FXML
    private Label progressionLabel;

    @FXML
    private TextArea projectDescription;

    @FXML
    private HBox basePane;

   @FXML
   private ScrollPane mainScrollPane;

    public void initialize() {
        setText();
        mainScrollPane.setFitToWidth(true);

        loadSteps();
        setLoadingBar(); //Set steps before this function
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
        int progressRange = ProjectController.calculateProgress(MAX_RANGE, totalStepCount, completedSteps);

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
               if (!step.getbIsCompleted()){
                   incompleteStepCount++;
               }
               createStep(step);
           }
           setRatio();
        }
    }

    public void setRatio(){
        totalStepCount = steps.size();
        completedSteps = totalStepCount - incompleteStepCount;
        String completedStepsString = "" + completedSteps;
        String totalStepsString = "" + totalStepCount;

        progressionLabel.setText(completedStepsString + "/" + totalStepsString);
    }

    public void createStep(ProjectStep step){
        newSteps.add(step);
        HBox hboxRow;

        if (newSteps.size() % 2 != 0){
            hboxRow = createHbox();
            stepBoxes.add(hboxRow);
            VBox leftSide = (VBox) hboxRow.getChildren().get(0);
            leftSide.setPrefSize(200, 200);
            leftSide.setAlignment(Pos.CENTER);
            HBox complete = new HBox();
            Label competeLabel = new Label(step.getbIsCompleted() ? "Completed" : "Incomplete");
            RadioButton completeRadio = new RadioButton();

            if (step.getbIsCompleted()){
                completeRadio.setSelected(true);
            }

            completeRadio.setOnAction(actionEvent -> {
                if (completeRadio.isSelected()){
                    step.setCompleted(true);
                    setRatio();
                }
                else{
                    step.setCompleted(false);
                    setRatio();
                }
            });
            Label nameLabel = new Label(step.getName());
            TextArea decriptionLabel = new TextArea(step.getDescription());

            complete.getChildren().addAll(competeLabel, completeRadio);
            leftSide.getChildren().addAll(complete, nameLabel, decriptionLabel);

            stepsPane.getChildren().add(hboxRow);
        }

        else{
            hboxRow = stepBoxes.getLast();
            VBox rightSide = (VBox) hboxRow.getChildren().get(1);
            rightSide.setPrefSize(200, 200);
            rightSide.setAlignment(Pos.CENTER);

            HBox complete = new HBox();
            Label competeLabel = new Label(step.getbIsCompleted() ? "Completed" : "Incomplete");
            RadioButton completeRadio = new RadioButton();

            if (step.getbIsCompleted()){
                completeRadio.setSelected(true);
            }

            completeRadio.setOnAction(actionEvent -> {
                if (completeRadio.isSelected()){
                    step.setCompleted(true);
                    setRatio();
                }
                else{
                    step.setCompleted(false);
                    setRatio();
                }
            });
            Label nameLabel = new Label(step.getName());
            TextArea decriptionLabel = new TextArea(step.getDescription());

            complete.getChildren().addAll(competeLabel, completeRadio);
            rightSide.getChildren().addAll(complete, nameLabel, decriptionLabel);
        }

    }

    public HBox createHbox(){
        HBox hboxRow = new HBox();
        hboxRow.setAlignment(Pos.CENTER);
        hboxRow.setSpacing(10);
        VBox leftSide = new VBox();
        VBox rightSide = new VBox();
        hboxRow.getChildren().addAll(leftSide, rightSide);
        return hboxRow;
    }

    public void onCreateStep(){

        Dialog<String> popup = new Dialog<>();
        popup.setTitle("Add Step");

        TextField name = new TextField();
        TextArea description = new TextArea();
        VBox popupPane = new VBox();
        popupPane.getChildren().addAll(name, description);
        popup.getDialogPane().setContent(popupPane);

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        popup.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);


        // Handle the Save button click and retrieve input data
        popup.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                // Combine or process the input values as needed
                try {
                    FirebaseRequestHandler.CreateProjectStep(loadedProject.getID(), name.getText(), description.getText());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            return null;
        });

        // Show the dialog and capture the result
        popup.showAndWait().ifPresent(result -> {
            System.out.println("User Details: " + result);
            // Further processing of the saved details here, e.g., updating UI or storing data
        });
    }


}
