package com.qut.cab302_a1;

import java.io.IOException;
import java.util.*;

import com.qut.cab302_a1.models.Project;
import firebase.FirebaseDataStorage;
import javafx.util.Pair;
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

        loadUserProjects();
    }


    public void printUserProjectsToConsole() {
        List<Project> userProjects = FirebaseDataStorage.getProjects();

        // Print the number of projects retrieved
        System.out.println("Total Projects Retrieved: " + userProjects.size());

        // Print each project's details
        for (Project project : userProjects) {
            System.out.println("---- Project ----");
            project.DebugProjectData(); // Use the method to print details
            System.out.println("---- End of Project ----");
        }
    }


    @FXML
    private CustomStackPane createProjectPaneWithData(String projectID, String projectName, String projectDescription, String projectResources, String projectTools) {
        CustomStackPane overlay = new CustomStackPane(testTitle);
        overlay.setProjectID(projectID);
        overlay.setId("overlayID");

        // Create main project pane for project overview
        VBox projectPane = createMainPaneWithoutBackend(overlay, projectName, projectDescription, projectResources, projectTools);

        // Create expanded (detailed) view
        HBox bigPane = createBigPane();
        bigPane.setVisible(false);  // Initially hidden until clicked

        // Set IDs for styling
        projectPane.setId("projectPane");
        bigPane.setId("bigPane");

        // Set rounded corner styles for both panes
        overlay.setStyle("-fx-background-radius: 20px; -fx-border-radius: 20px");
        projectPane.setStyle("-fx-background-radius: 20px; -fx-border-radius: 20px");
        bigPane.setStyle("-fx-background-radius: 20px; -fx-border-radius: 20px");

        // Click listener to toggle to bigPane (expanded view) when projectPane is clicked
        projectPane.setOnMouseClicked(actionEvent -> {
            hideAllPanes();  // Hide other panes when this one is clicked
            bigPane.setVisible(true);  // Show the detailed view
            projectPane.setVisible(false);  // Hide the project overview

            // Adjust size for the expanded view
            double scrollVal = mainScrollPane.getVvalue();
            bigPane.setPrefSize(260, 260);
            overlay.setPrefSize(260, 260);
            mainScrollPane.setVvalue(scrollVal);  // Maintain scroll position
        });

        // Click listener to revert back to projectPane when bigPane is clicked
        bigPane.setOnMouseClicked(actionEvent -> {
            bigPane.setVisible(false);  // Hide the expanded view
            projectPane.setVisible(true);  // Show the overview view
        });

        // Add both views to the overlay so they can toggle visibility
        overlay.getChildren().addAll(projectPane, bigPane);

        return overlay;
    }





    @FXML
    private VBox mainVbox;

    @FXML
    private ScrollPane mainScrollPane;

    private void loadUserProjects() {
        try {
            // Fetch all user projects from Firebase
            List<Project> projects = FirebaseDataStorage.getProjects();

            for (Project project : projects) {
                // Use the updated `createProjectPaneWithData` to avoid any database interaction
                CustomStackPane projectPane = createProjectPaneWithData(
                        project.id, project.name, project.description,
                        project.resources, project.tools
                );

                if (!mainVbox.getChildren().contains(projectPane)) {
                    mainVbox.getChildren().add(projectPane);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Error", "Failed to load user projects: " + e.getMessage());
        }
    }

    private Button createDeleteButton(CustomStackPane overlay) {
        Button deleteButton = new Button("Delete");
        deleteButton.getStyleClass().add("round-button");
        deleteButton.setOnAction(event -> {
            String projectID = overlay.getProjectID();

            if (projectID == null || projectID.isEmpty()) {
                showErrorAlert("Validation Error", "Project ID cannot be empty.");
                return;
            }

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
                try {
                    Boolean deletionResult = FirebaseRequestHandler.DeleteProject(projectID);

                    if (Boolean.TRUE.equals(deletionResult)) {
                        mainVbox.getChildren().remove(overlay);
                        paneObservers.remove(overlay);
                        projectList.remove(overlay);
                    } else {
                        showErrorAlert("Error Deleting Project", "Failed to delete project from the database.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    showErrorAlert("Exception", "Failed to delete the project: " + e.getMessage());
                }
            }
        });
        return deleteButton;
    }



    private CustomStackPane createDisplayOnlyPane(String projectID, String projectName,
                                                  String projectDescription,
                                                  String projectResources,
                                                  String projectTools) {
        CustomStackPane overlay = new CustomStackPane(testTitle);
        overlay.setProjectID(projectID);
        overlay.setId("overlayID");

        VBox projectPane = createMainPaneWithoutBackend(overlay, projectName,
                projectDescription,
                projectResources,
                projectTools);

        HBox bigPane = createBigPane();
        overlay.getChildren().addAll(projectPane, bigPane);
        return overlay;
    }

    private VBox createMainPaneWithoutBackend(CustomStackPane overlay, String projectName, String projectDescription, String projectResources, String projectTools) {
        VBox projectPane = new VBox(20);

        // Set up editable text fields
        TextField nameField = new TextField(projectName);
        TextField descriptionField = new TextField(projectDescription);
        TextField resourcesField = new TextField(projectResources);
        TextField toolsField = new TextField(projectTools);

        // Add focus listeners for automatic saving
        nameField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // Focus lost
                try {
                    FirebaseRequestHandler.UpdateProjectName(overlay.getProjectID(), nameField.getText());
                } catch (Exception e) {
                    e.printStackTrace();
                    showErrorAlert("Error", "Failed to update project name: " + e.getMessage());
                }
            }
        });

        descriptionField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // Focus lost
                try {
                    FirebaseRequestHandler.UpdateProjectDescription(overlay.getProjectID(), descriptionField.getText());
                } catch (Exception e) {
                    e.printStackTrace();
                    showErrorAlert("Error", "Failed to update project description: " + e.getMessage());
                }
            }
        });


        // Add text fields to the project pane
        projectPane.getChildren().addAll(nameField, descriptionField, resourcesField, toolsField);
        return projectPane;
    }


    public class CustomStackPane extends StackPane implements ObserverPane {
        private String projectID; // Add this variable to store the project ID
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

        public String getProjectID() {
            return projectID;
        }
        public void setProjectID(String projectID) {
            this.projectID = projectID;
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
    private CustomStackPane createProjectPane() {
        // Create a new CustomStackPane with a temporary ID
        CustomStackPane overLay = new CustomStackPane(testTitle);
        overLay.setId("overlayID");

        // Extract initial details from the text fields
        String projectName = "New Project"; // Default project name
        String projectDescription = "Enter description here";
        String projectResources = "Enter resources here";
        String projectTools = "Enter tools here";

        // Call the Firebase request to create a new project immediately
        try {
            Pair<String, String> rawResult = FirebaseRequestHandler.CreateProject(projectName, projectDescription);
            String result = rawResult.getKey();

            if (result.equals("success")) {
                // Set the generated project ID to the CustomStackPane
                overLay.setProjectID(rawResult.getValue());
            } else {
                showErrorAlert("Error", "Failed to create project: " + result);
            }
        } catch (Exception e) {
            e.printStackTrace();  // Log any errors
            showErrorAlert("Error", "An error occurred while creating the project: " + e.getMessage());
        }

        // Create the UI components for the new project pane
        VBox projectPane = createMainPane(overLay);
        HBox bigPane = createBigPane();
        projectPane.setId("projectPane");
        bigPane.setId("bigPane");

        // Rounding corners for UI aesthetics
        overLay.setStyle("-fx-background-radius: 20px; -fx-border-radius: 20px");
        projectPane.setStyle("-fx-background-radius: 20px; -fx-border-radius: 20px");
        bigPane.setStyle("-fx-background-radius: 20px; -fx-border-radius: 20px");

        // Add mouse-click listener for selecting/expanding the project pane
        projectPane.setOnMouseClicked(actionEvent -> {
            // Hide all other panes when selecting this project
            hideAllPanes();

            // Store the current scroll position to restore it after expansion
            double scrollVal = mainScrollPane.getVvalue();

            // Show the expanded view (bigPane) and hide the projectPane view
            bigPane.setVisible(true);
            projectPane.setVisible(false);

            // Adjust sizes to expand the selected pane
            bigPane.setPrefSize(260, 260);
            bigPane.layout();
            overLay.setPrefSize(260, 260);
            bigPane.layout();
            mainScrollPane.setVvalue(scrollVal); // Restore scroll position
        });

        // Optional listener to log dimensions when bigPane is clicked
        bigPane.setOnMouseClicked(actionEvent -> {
            System.out.println("BigPane Width: " + bigPane.getWidth() + ", Height: " + bigPane.getHeight());
        });

        // Add both projectPane and bigPane to the overlay
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

    // Method to handle the saving of duplicated projects to db and frontend
    private void handleDuplicateProjectSave(String projectName, String projectDescription, String projectResources, String projectTools) {
        try {
            // Create a duplicate in the backend
            Pair<String, String> rawResult = FirebaseRequestHandler.CreateProject(projectName, projectDescription);
            String result = rawResult.getKey();
            String newProjectID = rawResult.getValue();

            if (result.equals("success")) {
                // Show success message if saved to db
                showSuccessAlert("Project Duplicated", "The duplicated project was successfully saved to the database!");

                // Create a new pane for the duplicated project on the frontend
                CustomStackPane duplicateProjectPane = createProjectPaneWithData(
                        newProjectID, // Newly generated project ID
                        projectName,
                        projectDescription,
                        projectResources,
                        projectTools
                );

                // Add the duplicate project pane to mainVbox if it’s not already there
                if (!mainVbox.getChildren().contains(duplicateProjectPane)) {
                    mainVbox.getChildren().add(duplicateProjectPane);
                } else {
                    System.out.println("Duplicate panel already exists in mainVbox!");
                }

                hideAllPanes(); // Adjust pane sizes as needed

            } else {
                // Show error message if it fails
                showErrorAlert("Error", "Failed to duplicate project: " + result);
            }
        } catch (Exception e) {
            e.printStackTrace();  // Log any errors
            showErrorAlert("Error", "An error occurred while duplicating the project: " + e.getMessage());
        }
    }


    private VBox createMainPane(CustomStackPane overlay) {
        VBox projectPane = new VBox(20);
        projectPane.getStyleClass().add("project-pane");

        // Initial project details
        String projectName = "New Project";
        String projectDescription = "Enter description here";
        String projectResources = "Enter resources here";
        String projectTools = "Enter tools here";

        // Attempt to create a new project in the database immediately
        try {
            Pair<String, String> rawResult = FirebaseRequestHandler.CreateProject(projectName, projectDescription);
            String result = rawResult.getKey();

            if (result.equals("success")) {
                overlay.setProjectID(rawResult.getValue());
            } else {
                showErrorAlert("Error", "Failed to create project: " + result);
            }
        } catch (Exception e) {
            e.printStackTrace();  // Log any errors
            showErrorAlert("Error", "An error occurred while creating the project: " + e.getMessage());
        }

        // Create and set up text fields
        TextField projectNameField = new TextField(projectName);
        projectNameField.setPromptText("Enter project name");
        projectNameField.getStyleClass().add("project-text-field");

        TextField projectDescriptionField = new TextField(projectDescription);
        projectDescriptionField.setPromptText("Enter project description");
        projectDescriptionField.getStyleClass().add("project-text-field");

        TextField projectResourcesField = new TextField(projectResources);
        projectResourcesField.setPromptText("Enter list of required resources");
        projectResourcesField.getStyleClass().add("project-text-field");

        TextField projectToolsField = new TextField(projectTools);
        projectToolsField.setPromptText("Enter list of required tools");
        projectToolsField.getStyleClass().add("project-text-field");

        // Add focus listeners for automatic saving
        projectNameField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // Focus lost
                try {
                    FirebaseRequestHandler.UpdateProjectName(overlay.getProjectID(), projectNameField.getText());
                } catch (Exception e) {
                    e.printStackTrace();
                    showErrorAlert("Error", "Failed to update project name: " + e.getMessage());
                }
            }
        });

        projectDescriptionField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // Focus lost
                try {
                    FirebaseRequestHandler.UpdateProjectDescription(overlay.getProjectID(), projectDescriptionField.getText());
                } catch (Exception e) {
                    e.printStackTrace();
                    showErrorAlert("Error", "Failed to update project description: " + e.getMessage());
                }
            }
        });

        // Duplicate and Delete buttons
        Button duplicateButton = new Button("Duplicate");
        duplicateButton.getStyleClass().add("round-button");
        duplicateButton.setOnAction(event -> {
            CustomStackPane duplicateProjectPane = createProjectPane();
            duplicateProjectPane.setId("duplicateProjectPaneID");

            VBox newProjectPane = (VBox) duplicateProjectPane.getChildren().get(0);
            ((TextField) newProjectPane.getChildren().get(0)).setText(projectNameField.getText());
            ((TextField) newProjectPane.getChildren().get(1)).setText(projectDescriptionField.getText());
            ((TextField) newProjectPane.getChildren().get(2)).setText(projectResourcesField.getText());
            ((TextField) newProjectPane.getChildren().get(3)).setText(projectToolsField.getText());

            if (!mainVbox.getChildren().contains(duplicateProjectPane)) {
                mainVbox.getChildren().add(duplicateProjectPane);
            } else {
                System.out.println("Duplicate panel already exists in mainVbox!");
            }

            hideAllPanes();
        });

        // Attach the new delete button
        Button deleteButton = createDeleteButton(overlay);

        // Add buttons to the layout
        HBox buttonBox = new HBox(10, duplicateButton, deleteButton);
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
    protected void onCreatePanelAction() {
        System.out.println("Creating a new project in the backend!");

        // Attempt to create a new project in the backend first
        try {
            // Default project details for a new project
            String projectName = "New Project";
            String projectDescription = "Enter description here";

            // Create the project in the backend only once here
            Pair<String, String> rawResult = FirebaseRequestHandler.CreateProject(projectName, projectDescription);
            String result = rawResult.getKey();

            if (result.equals("success")) {
                // Pass the new project's ID and details to create a pane with these details
                CustomStackPane projectPane = createProjectPaneWithData(
                        rawResult.getValue(), // projectID
                        projectName,
                        projectDescription,
                        "", // Empty resources for new project
                        ""  // Empty tools for new project
                );

                if (!mainVbox.getChildren().contains(projectPane)) {
                    mainVbox.getChildren().add(projectPane);
                } else {
                    System.out.println("Project panel already exists in mainVbox!");
                }

                hideAllPanes(); // Adjust pane sizes as needed
            } else {
                showErrorAlert("Error", "Failed to create project: " + result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Error", "An error occurred while creating the project: " + e.getMessage());
        }
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

