package com.qut.cab302_a1;

import java.io.IOException;
import java.util.*;
import firebase.FirebaseRequestHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;



public class ProjectController   {
    private List<CustomStackPane> projectList = new ArrayList<>();
    private static List<ObserverPane> paneObservers =  new ArrayList<>();
    public static int testTitle = 10;

    private CustomStackPane activePane = null; // The currently active panel

    @FXML
    private Button buttonV;

    @FXML
    private Hyperlink hyperlink0, hyperlink1, hyperlink2, hyperlink3;
    Hyperlink[] hyperlinks = new Hyperlink[4];

    @FXML
    private Label sidepart0, sidepart05, sidepart1, sidepart15, sidepart2, sidepart25, sidepart3;
    Label[] sidepartLabels = new Label[7];


    @FXML
    private void minimiseActon() {
        if (hyperlink1.isVisible()) {
            for (Hyperlink link : hyperlinks) {
                link.setVisible(false);
            }
            for (Label label : sidepartLabels) {
                label.setVisible(false);
            }
            buttonV.setText("-");
        }
        else{
            for (Hyperlink link : hyperlinks) {
                link.setVisible(true);
            }
            for (Label label : sidepartLabels) {
                label.setVisible(true);
            }
            buttonV.setText("v");
        }
    }

    //edit keyboard shortcut

    private void fireEditActivePanel() {
        if (activePane == null) {
            System.out.println("No active project pane to edit");
            return;
        }


        // Get the relevant fields from the active pane
        VBox projectPane = (VBox) activePane.getChildren().get(0);
        TextField projectNameField = (TextField) projectPane.getChildren().get(0);
        TextField projectDescriptionField = (TextField) projectPane.getChildren().get(1);
        TextField projectResourcesField = (TextField) projectPane.getChildren().get(2);
        TextField projectToolsField = (TextField) projectPane.getChildren().get(3);

        // Set the fields to be editable
        projectNameField.setEditable(true);
        projectDescriptionField.setEditable(true);
        projectResourcesField.setEditable(true);
        projectToolsField.setEditable(true);

        System.out.println("Project pane editing enabled via Ctrl + E");
    }




    //duplicate keyboard shorcut
    private void fireDuplicateActivePanel() {
        if (activePane == null) {
            System.out.println("No active project pane to duplicate");
            return;
        }

        // Get the field values from the active pane
        VBox projectPane = (VBox) activePane.getChildren().get(0);
        TextField projectNameField = (TextField) projectPane.getChildren().get(0);
        TextField projectDescriptionField = (TextField) projectPane.getChildren().get(1);
        TextField projectResourcesField = (TextField) projectPane.getChildren().get(2);
        TextField projectToolsField = (TextField) projectPane.getChildren().get(3);

        String projectName = projectNameField.getText();
        String projectDescription = projectDescriptionField.getText();
        String projectResources = projectResourcesField.getText();
        String projectTools = projectToolsField.getText();

        // Create a new CustomStackPane (duplicate) without automatically adding it to mainVbox
        CustomStackPane duplicatePane = createProjectPane();
        duplicatePane.setId("duplicateProjectPaneID");

        // Set the text fields on the duplicate pane
        VBox newProjectPane = (VBox) duplicatePane.getChildren().get(0);
        ((TextField) newProjectPane.getChildren().get(0)).setText(projectName);
        ((TextField) newProjectPane.getChildren().get(1)).setText(projectDescription);
        ((TextField) newProjectPane.getChildren().get(2)).setText(projectResources);
        ((TextField) newProjectPane.getChildren().get(3)).setText(projectTools);

        // Add the duplicate pane to mainVbox only if it's not already added
        if (!mainVbox.getChildren().contains(duplicatePane)) {
            mainVbox.getChildren().add(duplicatePane);
        } else {
            System.out.println("Duplicate panel already exists in mainVbox!");
        }

        System.out.println("Project pane duplicated via Ctrl + D");
    }


    //delete shortcut
    private void fireDeleteActivePanel() {
        if (activePane == null) {
            System.out.println("No active project pane to delete");
            return;
        }

        // Show confirmation dialog before deleting
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("Are you sure you want to delete this project?");
        alert.setContentText("This action cannot be undone.");

        ButtonType deleteButtonType = new ButtonType("Delete");
        ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(deleteButtonType, cancelButtonType);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == deleteButtonType) {
            // Perform the deletion
            mainVbox.getChildren().remove(activePane);  // Remove pane from the UI
            paneObservers.remove(activePane);           // Remove from observer list
            projectList.remove(activePane);             // Remove from project list
            System.out.println("Project pane deleted via Ctrl + Del");
            activePane = null;  // Reset activePane after deletion
        } else {
            System.out.println("Deletion cancelled");
        }
    }


    private void fireSaveActivePanel() {
        if (activePane == null) {
            System.out.println("No active project pane to save");
            return;
        }

        // Get the project fields from the active pane
        VBox projectPane = (VBox) activePane.getChildren().get(0);
        TextField projectNameField = (TextField) projectPane.getChildren().get(0);
        TextField projectDescriptionField = (TextField) projectPane.getChildren().get(1);
        TextField projectResourcesField = (TextField) projectPane.getChildren().get(2);
        TextField projectToolsField = (TextField) projectPane.getChildren().get(3);
        Button saveButton = (Button) projectPane.lookup(".save-button"); // Adjust selector as needed

        String projectName = projectNameField.getText();
        String projectDescription = projectDescriptionField.getText();
        String projectResources = projectResourcesField.getText();
        String projectTools = projectToolsField.getText();

        // Validate that necessary fields are filled
        if (!projectName.isEmpty() && !projectDescription.isEmpty() && !projectResources.isEmpty() && !projectTools.isEmpty()) {
            try {
                String result = FirebaseRequestHandler.CreateProject(projectName, projectDescription);

                if (result.equals("success")) {
                    // Disable fields after saving, similar to Save button behavior
                    projectNameField.setEditable(false);
                    projectDescriptionField.setEditable(false);
                    projectResourcesField.setEditable(false);
                    projectToolsField.setEditable(false);

                    // Fade the Save button by disabling it
                    if (saveButton != null) {
                        saveButton.setDisable(true); // This should apply the fade effect
                    }

                    showSuccessAlert("Project Saved", "Project was successfully saved to the database!");
                } else {
                    showErrorAlert("Error saving project", result);
                }
            } catch (Exception e) {
                showErrorAlert("Exception", "Failed to save the project: " + e.getMessage());
            }
        } else {
            showErrorAlert("Validation Error", "Project name and description cannot be empty.");
        }
    }



    @FXML
    private HBox basePane;

    @FXML
    public void initialize(){
        try{
            basePane.getStylesheets().add(getClass().getResource("stylesheets/projectControllerStyle.css").toExternalForm());
        }
        catch (Exception e){
            System.out.println("Stylesheet failed to load");
        }
        mainScrollPane.setFitToWidth(true);
        mainScrollPane.setFitToHeight(true);
        mainVbox.setFillWidth(true);
        hyperlinks = new Hyperlink[] {hyperlink0, hyperlink1, hyperlink2, hyperlink3};
        sidepartLabels = new Label[] {sidepart0, sidepart05, sidepart1, sidepart15, sidepart2, sidepart25, sidepart3};

        // Set up key listener for Ctrl + Del
        mainVbox.setOnKeyPressed(event -> {
            if (new KeyCodeCombination(KeyCode.DELETE, KeyCombination.CONTROL_DOWN).match(event)) {
                fireDeleteActivePanel();
                event.consume(); // Consume the event so no further processing occurs
            }else if (new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN).match(event)) {
                fireSaveActivePanel();
                event.consume();
            }else if (new KeyCodeCombination(KeyCode.D, KeyCombination.CONTROL_DOWN).match(event)) {
                fireDuplicateActivePanel();
                event.consume();
            }else if (new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN).match(event)) {
                fireEditActivePanel();
                event.consume();
            }      // Check for various key codes that could represent "+"


        });

        // Setup scene-level key event for Ctrl + "+" shortcut
        mainVbox.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
                    if (new KeyCodeCombination(KeyCode.ADD, KeyCombination.CONTROL_DOWN).match(event) ||
                            new KeyCodeCombination(KeyCode.PLUS, KeyCombination.CONTROL_DOWN).match(event) ||
                            new KeyCodeCombination(KeyCode.EQUALS, KeyCombination.CONTROL_DOWN).match(event)) {

                        System.out.println("Ctrl + '+' detected at scene level, creating new project pane."); // Debug
                        onCreatePanelAction(); // Trigger pane creation
                        event.consume();
                    }
                });
            }
        });

        // Ensure mainVbox can receive key events
        mainVbox.setFocusTraversable(true);

    }

    @FXML
    private VBox mainVbox;

    @FXML
    private ScrollPane mainScrollPane;


    public class CustomStackPane extends StackPane implements ObserverPane {

        int position; //set to 0 later
        public CustomStackPane(int position) {
            super();
            this.position = position;
            paneObservers.add(this);
            projectList.add(this);

            //test
            for(CustomStackPane pane: projectList){
                System.out.print(pane.getPosition() + ", ");
            }

            System.out.println("");

            notifiedObservers();

            for (CustomStackPane pane: projectList) {
                mainVbox.getChildren().remove(pane);
            }


            for (CustomStackPane pane: projectList){
                mainVbox.getChildren().add(pane);
            }


            //test
            for(CustomStackPane pane: projectList){
                System.out.print(pane.getPosition() + ", ");
            }

        }

        public int getPosition(){
            return position;
        }

        public void setPosition(int positionVal){
            this.position = positionVal;
        }

        // When a postion is changed this function is called which updates each observer
        public void notifiedObservers() {
            System.out.println(testTitle);
            mergeOrderObservers();
        }

        // change location
        @Override
        public void updatePane(int position) {
            System.out.println("position = " + position);
        }

        public void mergeOrderObservers(){
            projectList = splitList(projectList);
        }

        public List<CustomStackPane> splitList(List<CustomStackPane> list){
            List<CustomStackPane> firstHalf = new ArrayList<>();
            List<CustomStackPane> secondHalf = new ArrayList<>();
            List<CustomStackPane> finalList = new ArrayList<>();

            if (list.size() < 2){
                return list;
            }
            else{
                int half = list.size() / 2;

                for (int i = 0; i < half; i++) {
                    firstHalf.add(list.get(i));
                }

                for (int i = half; i < list.size(); i++) {
                    secondHalf.add(list.get(i));
                }

                firstHalf = splitList(firstHalf);
                secondHalf = splitList(secondHalf);
            }

            return mergeLists(list, firstHalf, secondHalf);
        }

        public List<CustomStackPane> mergeLists(List<CustomStackPane> original, List<CustomStackPane> first, List<CustomStackPane> second){
            List<CustomStackPane> merged = new ArrayList<>();
            int originalSize = original.size();
            int i = 0, j = 0;

            while (i < first.size() && j < second.size()){
                if (first.get(i).getPosition() < second.get(j).getPosition()){
                    merged.add(first.get(i));
                    i++;
                }
                else {
                    merged.add(second.get(j));
                    j++;
                }
            }

            while (i < first.size()){
                merged.add(first.get(i));
                i++;
            }

            while (j < second.size()){
                merged.add(second.get(j));
                j++;
            }


            // 2 for loops
            return merged;
        }
    }
    /**
     * main functionality to the page. Creates the ui for the page
     *
     * @return overlay pane (base pane)
     */
    @FXML
    private CustomStackPane createProjectPane (){
        CustomStackPane overLay = new CustomStackPane(testTitle);
        overLay.setId("overlayID");
        VBox projectPane = createMainPane(overLay);

        // Set the active panel when clicked
        projectPane.setOnMouseClicked(event -> activePane = overLay);

        HBox bigPane = createBigPane();
        projectPane.setId("projectPane");
        bigPane.setId("bigPane");
        //Rounding corners
        overLay.setStyle("-fx-background-radius: 20px; -fx-border-radius: 20px");
        projectPane.setStyle("-fx-background-radius: 20px; -fx-border-radius: 20px");
        bigPane.setStyle("-fx-background-radius: 20px; -fx-border-radius: 20px");
// Set the active panel when the project pane itself is clicked
        projectPane.setOnMouseClicked(event -> {
            activePane = overLay;
            System.out.println("Active pane set via pane click: " + activePane);
        });

// Add listeners to each field to set activePane when focused
        TextField projectNameField = (TextField) projectPane.getChildren().get(0);
        TextField projectDescriptionField = (TextField) projectPane.getChildren().get(1);
        TextField projectResourcesField = (TextField) projectPane.getChildren().get(2);
        TextField projectToolsField = (TextField) projectPane.getChildren().get(3);

        projectNameField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                activePane = overLay;
                System.out.println("Active pane set via projectNameField focus: " + activePane);
            }
        });
        projectDescriptionField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                activePane = overLay;
                System.out.println("Active pane set via projectDescriptionField focus: " + activePane);
            }
        });
        projectResourcesField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                activePane = overLay;
                System.out.println("Active pane set via projectResourcesField focus: " + activePane);
            }
        });
        projectToolsField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                activePane = overLay;
                System.out.println("Active pane set via projectToolsField focus: " + activePane);
            }
        });

        // Add key event listener to each field to detect Ctrl + Del while editing
        projectNameField.setOnKeyPressed(event -> {
            if (new KeyCodeCombination(KeyCode.DELETE, KeyCombination.CONTROL_DOWN).match(event)) {
                fireDeleteActivePanel();
                event.consume();
            }
        });
        projectDescriptionField.setOnKeyPressed(event -> {
            if (new KeyCodeCombination(KeyCode.DELETE, KeyCombination.CONTROL_DOWN).match(event)) {
                fireDeleteActivePanel();
                event.consume();
            }
        });
        projectResourcesField.setOnKeyPressed(event -> {
            if (new KeyCodeCombination(KeyCode.DELETE, KeyCombination.CONTROL_DOWN).match(event)) {
                fireDeleteActivePanel();
                event.consume();
            }
        });
        projectToolsField.setOnKeyPressed(event -> {
            if (new KeyCodeCombination(KeyCode.DELETE, KeyCombination.CONTROL_DOWN).match(event)) {
                fireDeleteActivePanel();
                event.consume();
            }
        });

        // Add key event listeners for Ctrl + D to each field
        projectNameField.setOnKeyPressed(event -> {
            if (new KeyCodeCombination(KeyCode.D, KeyCombination.CONTROL_DOWN).match(event)) {
                fireDuplicateActivePanel();
                event.consume();
            }
        });
        projectDescriptionField.setOnKeyPressed(event -> {
            if (new KeyCodeCombination(KeyCode.D, KeyCombination.CONTROL_DOWN).match(event)) {
                fireDuplicateActivePanel();
                event.consume();
            }
        });
        projectResourcesField.setOnKeyPressed(event -> {
            if (new KeyCodeCombination(KeyCode.D, KeyCombination.CONTROL_DOWN).match(event)) {
                fireDuplicateActivePanel();
                event.consume();
            }
        });
        projectToolsField.setOnKeyPressed(event -> {
            if (new KeyCodeCombination(KeyCode.D, KeyCombination.CONTROL_DOWN).match(event)) {
                fireDuplicateActivePanel();
                event.consume();
            }
        });

        // Lambda function that handles expanding the vbox when clicked.
        projectPane.setOnMouseClicked(actionEvent -> {
            hideAllPanes();
            //animation goes here

            double scrollVal = mainScrollPane.getVvalue();
            bigPane.setVisible(true);
            projectPane.setVisible(false);
            bigPane.setPrefSize(260, 260);

            bigPane.layout();

            overLay.setPrefSize(260, 260);
            bigPane.layout();


            mainScrollPane.setVvalue(scrollVal);
        });

        bigPane.setOnMouseClicked(actionEvent -> {
            System.out.println("BigPane Width: " + bigPane.getWidth() + ", Height: " + bigPane.getHeight());
        });

        overLay.getChildren().addAll(projectPane, bigPane);
        return overLay;
    }

    /**
     * Used before expanding a new pane into bigPane.
     *
     * loops through the list of parent stackPane.
     * gets both children small and big pane.
     *
     * Sets them to visible true or false. Changes the size so the stackPane isnt stretched.
     */
    private void hideAllPanes(){
        if (projectList.size() >= 3){
            mainScrollPane.setFitToHeight(false); // Disgusting fix to a really ugly big. DO NOT REMOVE
        }
        else if (projectList.size() <3){
            mainScrollPane.setFitToHeight(true);
        }

        for (CustomStackPane pane : projectList) {
            Node temp = pane.getChildren().get(1);
            ((HBox) temp).setPrefSize(150, 150);
            pane.setPrefSize(150, 150);
            temp.setVisible(false);
            temp = pane.getChildren().get(0);
            temp.setVisible(true);
        }
    }

    // Method to handle the saving of duplicated projects to db
    private void handleDuplicateProjectSave(String projectName, String projectDescription, String projectResources, String projectTools) {
        try {
            // This calls the FirebaseRequestHandler to save the project in the db
            String result = FirebaseRequestHandler.CreateProject(projectName, projectDescription); // EDITED OUT projectResources and projectTools

            if (result.equals("success")) {
                // Show success message if saved to db
                showSuccessAlert("Project Duplicated", "The duplicated project was successfully saved to the database!");
            } else {
                // Show error message if it fails
                showErrorAlert("Error", "Failed to duplicate project: " + result);
            }
        } catch (Exception e) {
            e.printStackTrace();  // Log any errors
            showErrorAlert("Error", "An error occurred while duplicating the project: " + e.getMessage());
        }
    }

    private VBox createMainPane(CustomStackPane overLay) {
        VBox projectPane = new VBox(20);
        projectPane.getStyleClass().add("project-pane");

        // Project details
        TextField projectNameField = new TextField();
        projectNameField.setPromptText("Enter project name");
        projectNameField.getStyleClass().add("project-text-field");

        TextField projectDescriptionField = new TextField();
        projectDescriptionField.setPromptText("Enter project description");
        projectDescriptionField.getStyleClass().add("project-text-field");

        TextField projectResourcesField = new TextField();
        projectResourcesField.setPromptText("Enter list of required resources");
        projectResourcesField.getStyleClass().add("project-text-field");

        TextField projectToolsField = new TextField();
        projectToolsField.setPromptText("Enter list of required tools");
        projectToolsField.getStyleClass().add("project-text-field");

        // Save Button
        Button saveButton = new Button("Save");
        saveButton.getStyleClass().add("round-button");
        saveButton.setOnAction(event -> {
            String projectName = projectNameField.getText();
            String projectDescription = projectDescriptionField.getText();
            String projectResources = projectResourcesField.getText();
            String projectTools = projectToolsField.getText();

            if (!projectName.isEmpty() && !projectDescription.isEmpty() && !projectResources.isEmpty() && !projectTools.isEmpty()) {
                try {
                    String result = FirebaseRequestHandler.CreateProject(projectName, projectDescription);

                    if (result.equals("success")) {
                        // Success case: disable editing and buttons
                        projectNameField.setEditable(false);
                        projectDescriptionField.setEditable(false);
                        projectResourcesField.setEditable(false);
                        projectToolsField.setEditable(false);
                        saveButton.setDisable(true);

                        showSuccessAlert("Project Created", "Project was successfully saved to the database!");
                    } else {
                        showErrorAlert("Error creating project", result);
                    }
                } catch (Exception e) {
                    showErrorAlert("Exception", "Failed to save the project: " + e.getMessage());
                }
            } else {
                showErrorAlert("Validation Error", "Project name and description cannot be empty.");
            }
        });

        // Duplicate button
        Button duplicateButton = new Button("Duplicate");
        duplicateButton.getStyleClass().add("round-button");
        duplicateButton.setOnAction(event -> {
            String projectName = projectNameField.getText();
            String projectDescription = projectDescriptionField.getText();
            String projectResources = projectResourcesField.getText();
            String projectTools = projectToolsField.getText();

            CustomStackPane duplicateProjectPane = createProjectPane();
            duplicateProjectPane.setId("duplicateProjectPaneID");

            VBox newProjectPane = (VBox) duplicateProjectPane.getChildren().get(0);
            ((TextField) newProjectPane.getChildren().get(0)).setText(projectName);
            ((TextField) newProjectPane.getChildren().get(1)).setText(projectDescription);
            ((TextField) newProjectPane.getChildren().get(2)).setText(projectResources);
            ((TextField) newProjectPane.getChildren().get(3)).setText(projectTools);

            if (!mainVbox.getChildren().contains(duplicateProjectPane)) {
                mainVbox.getChildren().add(duplicateProjectPane);
            } else {
                System.out.println("Duplicate panel already exists in mainVbox!");
            }

            hideAllPanes();
            handleDuplicateProjectSave(projectName, projectDescription, projectResources, projectTools);
        });

        // Edit Button
        Button editButton = new Button("Edit");
        editButton.getStyleClass().add("round-button");
        editButton.setOnAction(event -> {
            projectNameField.setEditable(true);
            projectDescriptionField.setEditable(true);
            projectResourcesField.setEditable(true);
            projectToolsField.setEditable(true);
            saveButton.setDisable(false);
        });

        // Delete Button
        Button deleteButton = new Button("Delete");
        deleteButton.getStyleClass().add("round-button");
        deleteButton.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Are you sure you want to delete this project?");
            alert.setContentText("This action cannot be undone.");

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            alert.initOwner(stage);

            ButtonType deleteButtonType = new ButtonType("Delete");
            ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(deleteButtonType, cancelButtonType);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == deleteButtonType) {
                mainVbox.getChildren().remove(overLay);
                paneObservers.remove(overLay);
                projectList.remove(overLay);
            }
        });

        // Adding buttons to a horizontal box layout
        HBox buttonBox = new HBox(10, saveButton, editButton, deleteButton, duplicateButton);
        projectPane.getChildren().addAll(projectNameField, projectDescriptionField, projectResourcesField, projectToolsField, buttonBox);

        return projectPane;
    }


    // Method to display success alerts
    private void showSuccessAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);  // No header, just a title and content
        alert.setContentText(message);
        alert.showAndWait();  // Wait for the user to close the alert
    }

    // Method to display error alerts
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);  // No header, just a title and content
        alert.setContentText(message);
        alert.showAndWait();  // Wait for the user to close the alert
    }


    /**
     * Creates the bigPane.
     *
     * bigPane is split between 2 sides and uses multiple containers to that it's content
     * is present like the high fidelity prototype.
     *
     * Spacing has been set to a html style for readability.
     *
     * Pulls an image from resources/pictures
     * @return bigPane expanded version of the projectPane
     */
    private HBox createBigPane(){
        HBox bigPane = new HBox(20);

        int totalProgress = 10; // testing values
        int currentProgress = 9; //testing values

        bigPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, null)));
        bigPane.setVisible(false);

        VBox pictureBox = new VBox(20);
        pictureBox.fillWidthProperty();
        // top, right?, left?, bottom?
        pictureBox.setPadding(new Insets(25, 0, 0, 25));
        Image placeholder = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/qut/cab302_a1/pictures/forge.png")));
        ImageView imageView = new ImageView(placeholder);
        imageView.setFitHeight(180);
        imageView.setFitWidth(180);
        imageView.setStyle("-fx-background-radius: 20px;"); //come back to this later.
        imageView.setDisable(true);

        pictureBox.getChildren().addAll(imageView);
        //Right side
        VBox rightSide = new VBox(20);

        //Left side of rightSide panel
        rightSide.setPadding(new Insets(40, 0, 0, 0));
        HBox middlePane = new HBox(20);
        Label title = new Label("Title" + testTitle); // no more than 25 Char
        testTitle--;
        int MAX_SPACING = 400;
        title.setId("titleID");

        // progressbar right side of right side
        VBox progressBar = new VBox(20);
        //top row
        HBox progressBox = new HBox(20);
        progressBox.setSpacing(185);
        // = should be changed to a settings icon
        Label progressLabel = new Label("=  Progress");

        progressLabel.setId("progressLabelID");
        Label tipsLabel = new Label(currentProgress + "/" + totalProgress);
        tipsLabel.setId("tipsLabelID");

        progressBox.getChildren().addAll(progressLabel, tipsLabel);
        final int MAX_RANGE = 270;
        final int MAX_WIDTH = 5;
        int progressRange = calculateProgress(MAX_RANGE, totalProgress, currentProgress);

        HBox.setHgrow(middlePane, Priority.ALWAYS); // figure this out later. Meant to be growth between label and progressPane.
        StackPane progressPane = new StackPane();
        Rectangle backBar = new Rectangle(MAX_RANGE, MAX_WIDTH+0.2);

        backBar.setArcWidth(10);
        backBar.setArcHeight(2);
        backBar.setFill(Color.GRAY);

        //progress bar gradient should range from blue to red.
        Rectangle progressionBar = new Rectangle(progressRange, MAX_WIDTH);
        Color colorPicker = pickColor(MAX_RANGE, progressRange);

        progressionBar.setFill(colorPicker);
        backBar.setArcWidth(10);
        backBar.setArcHeight(2);

        progressPane.setAlignment(Pos.CENTER_LEFT);
        progressPane.getChildren().addAll(backBar, progressionBar);

        progressBar.getChildren().addAll(progressBox, progressPane);
        middlePane.getChildren().addAll(title, progressBar);

        // Text needs to be rendered before .getWidth runlater waits for UI changes before execute.
        String titleString = title.toString();
        double titleWidth = calculateTextSize(titleString);
        middlePane.setSpacing(calcSpacing(titleWidth, MAX_SPACING, title)); // HERE

        TextArea textArea = getTextArea();
        rightSide.getChildren().addAll(middlePane, textArea);

        bigPane.getChildren().addAll(pictureBox, rightSide);
        bigPane.setMinSize(150, 150);
        return bigPane;
    }


    public void movePanes(){
        if (testTitle % 2 == 0){
            //moving panes around method values above are for testing
        }
    }

    /**Gets the string of title and sets it to a text format
     * checks the width and returns double value
     *
     * @param title
     * @return size of text and font
     */
    public double calculateTextSize(String title){

        Text titleText = new Text(title);
        titleText.setFont(Font.font("-fx-font-size: 1.5"));
        double width = titleText.getLayoutBounds().getWidth();
        return width;
    }

    /**
     * Calculates the spacing for inbeteeen the title and the
     * progress bar.
     *
     * @param textSize size of text
     * @param maxSize maxSize of the space.
     * @param title title label
     * @return the size of the spacing
     */
    public int calcSpacing(double textSize, int maxSize, Label title){
        int newSize;
        double fontSize = 1.5; //css font size value
        if (textSize > maxSize){
            fontSize -= 0.2;
            title.setStyle("-fx-font-size: " + fontSize + "em;");
            System.out.println("adjusted font size: " + fontSize);
        }

        System.out.println(textSize);
        //System.out.println(calculateTextSize("Example") + " Test");
        newSize = maxSize - (int)textSize;
        System.out.println(newSize);

        return newSize;
    }

    public static TextArea getTextArea() {
        TextArea textArea = new TextArea("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.");
        textArea.setId("textareaID");
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setMaxWidth(520.00);
        textArea.setMaxHeight(100.00);
        return textArea;
    }


    /**Algorthim that determines how big the progress bar is
     * by dividing max_range by totalProgress and multiplying that with
     * currentProgress.
     *
     * Validation is also included.
     *
     * @param MAX_RANGE
     * @param totalProgress
     * @param currentProgress
     * @return size of the bar
     */
    public int calculateProgress(int MAX_RANGE, int totalProgress, int currentProgress){
        if (currentProgress > totalProgress || totalProgress == currentProgress || totalProgress == 0){
            return MAX_RANGE;
        }

        if (currentProgress != totalProgress && currentProgress == 0){
            return 0;
        }

        int range = MAX_RANGE / totalProgress;
        return (int)(range * currentProgress);
    }

    /**
     * Gets the color for the progress bar by comparing the size of
     * the bar compared to the full bar
     *
     * @param MAX_RANGE
     * @param progressRange
     * @return Color for progressBar
     */
    public Color pickColor(int MAX_RANGE, int progressRange){
        if (MAX_RANGE / 2 <= progressRange){ //Gradient this
            return Color.BLUE;
        }
        else{
            return Color.RED;
        }
    }


    @FXML
    protected void onCreatePanelAction(){
        System.out.println("Created Panel!");
        CustomStackPane projectPan = createProjectPane();


        //mainVbox.getChildren().add(projectPan);
        hideAllPanes(); // This for some reason fixes the size of panes.

    }

    @FXML
    private Button logoutButton;

    @FXML
    protected void onLogoutAction(){
        try{
            Stage stage = (Stage) logoutButton.getScene().getWindow();
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

//TODO
// Give each pane a priority number for rerangement.

