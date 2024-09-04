package com.qut.cab302_a1;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.Node;
import javafx.stage.Popup;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar;
import javafx.stage.Stage;
import java.util.Optional;

import java.util.ArrayList;
import java.util.List;

public class ProjectController {
    private List<StackPane> projectList = new ArrayList<>();
    private int identifier = 0;

    @FXML
    public void initialize(){
        mainScrollPane.setFitToWidth(true);
        mainVbox.setFillWidth(true);
    }

    @FXML
    private VBox mainVbox;

    @FXML
    private ScrollPane mainScrollPane;


    @FXML
    private StackPane createProjectPane(){
        StackPane overLay = new StackPane();
        VBox projectPane = createMainPane(overLay);
        VBox bigPane = createBigPane();
        projectList.add(overLay);

        // Lambda function that handles expanding the vbox when clicked.
        projectPane.setOnMouseClicked(actionEvent -> {


            final double scrollVal = mainScrollPane.getVvalue();

            //animation goes here
            bigPane.setVisible(true);
            projectPane.setVisible(false);
            bigPane.setPrefSize(450, 450);
            bigPane.layout();

            // Gets the overlay height get the location scroll position
            double inScrollPanePos = overLay.getBoundsInParent().getMinY();
            double contentHeight = mainScrollPane.getContent().getBoundsInLocal().getHeight();
            double scrollPaneVal = inScrollPanePos / (contentHeight - mainScrollPane.getViewportBounds().getHeight());


            mainScrollPane.setVvalue(scrollPaneVal);


            for (StackPane pane: projectList){
                if (pane != overLay){
                    System.out.println("hidden");
                    pane.setVisible(false);
                }
            }
        });

        Button exitButton = new Button("Exit");
        Button materials = new Button("Materials");
        exitButton.setOnAction((ActionEvent event) -> {
            final double scrollVal = mainScrollPane.getVvalue();
            projectPane.setVisible(true);
            bigPane.setVisible(false);
            bigPane.setPrefSize(150, 150);
            mainScrollPane.setVvalue(scrollVal);

            for (StackPane pane: projectList){
                System.out.println("revealed");
                pane.setVisible(true);
            }
        });
        // Material menu
        Popup popupMenu = createPopUp();
        popupMenu.hide();
        materials.setOnAction((ActionEvent event) -> {
            // button to get materials to show up.
            // TEMP stackoverflow recommends connecting popup the button but this will eventually be changed
            // TEMP but eventually when i figure out how to ill position the popup menu to a desired location.
            if (!popupMenu.isShowing()) {
                popupMenu.show(
                        materials,
                        materials.localToScreen(materials.getBoundsInLocal()).getMinX(),
                        materials.localToScreen(materials.getBoundsInLocal()).getMaxY());
            }
            else{
                popupMenu.hide();
            }
        });
        bigPane.getChildren().addAll(exitButton, materials);

        // stackPane adds above panels
        overLay.getChildren().addAll(projectPane, bigPane);

        return overLay;
    }

    private VBox createMainPane(StackPane overLay) {
        VBox projectPane = new VBox(20);
        projectPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, null)));
        projectPane.setPrefSize(150, 150);

        // Project Name
        TextField projectNameField = new TextField();
        projectNameField.setPromptText("Enter project name");

        TextField projectDescriptionField = new TextField();
        projectDescriptionField.setPromptText("Enter project description");

        // Save Button
        Button saveButton = new Button("Save");
        saveButton.setOnAction(event -> {
            String projectName = projectNameField.getText();
            if (!projectName.isEmpty()) {
                projectNameField.setEditable(false);
                projectDescriptionField.setEditable(false);
                saveButton.setDisable(true);
            }
        });

        // Edit Button
        Button editButton = new Button("Edit");
        editButton.setOnAction(event -> {
            projectNameField.setEditable(true);
            projectDescriptionField.setEditable(true);
            saveButton.setDisable(false);
        });

        //Delete Button
        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(event -> {
            // Create a confirmation alert
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Are you sure you want to delete this project?");
            alert.setContentText("This action cannot be undone.");

            // Get the current window (stage)
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            alert.initOwner(stage);  // Center the alert relative to the current window

            ButtonType deleteButtonType = new ButtonType("Delete");
            ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(deleteButtonType, cancelButtonType);

            ButtonBar buttonBar = (ButtonBar) alert.getDialogPane().lookup(".button-bar");
            buttonBar.setButtonOrder(ButtonBar.BUTTON_ORDER_NONE);
            buttonBar.setStyle("-fx-alignment: center;");

            //User Input
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == deleteButtonType) {
                mainVbox.getChildren().remove(overLay);
                projectList.remove(overLay);  // Remove from the list of projects
            }
        });

        // Adding buttons to a horizontal box layout
        HBox buttonBox = new HBox(10, saveButton, editButton, deleteButton);
        projectPane.getChildren().addAll(projectNameField, projectDescriptionField, buttonBox);

        // Highlight effect on hover
        projectPane.setOnMouseEntered(event -> {
            projectPane.setStyle("-fx-border-color: blue; -fx-border-width: 2;");
        });

        projectPane.setOnMouseExited(event -> {
            projectPane.setStyle("-fx-border-color: black; -fx-border-width: null;");
        });


        return projectPane;
    }




    private VBox createBigPane(){
        VBox bigPane = new VBox(20);
        bigPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, null)));
        bigPane.setVisible(false);
        Label exampleLabel = new Label("Example");
        bigPane.getChildren().addAll(exampleLabel);
        return bigPane;
    }

    private Popup createPopUp(){
        Popup popupMenu = new Popup();
        AnchorPane popupPane = new AnchorPane();
        popupPane.setPrefSize(450, 150);
        Label testLabel = new Label("Test");
        popupMenu.getContent().add(testLabel);
        popupPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, null)));
        popupMenu.getContent().add(popupPane);
        return popupMenu;
    }

    @FXML
    protected void onCreatePanelAction(){
        System.out.println("Created Panel!");
        StackPane projectPan = createProjectPane();

        mainVbox.getChildren().add(projectPan);

    }
}

//TODO refactor into a more MVC style
