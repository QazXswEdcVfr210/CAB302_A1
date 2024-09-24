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
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;



public class ProjectController   {
    private List<StackPane> projectList = new ArrayList<>();
    private static List<ObserverPane> paneObservers =  new ArrayList<>();
    public int testTitle = 1;

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
    }

    @FXML
    private VBox mainVbox;

    @FXML
    private ScrollPane mainScrollPane;


    public class CustomStackPane extends StackPane implements ObserverPane {

        int position = 0;
        public CustomStackPane(int position) {
            this.position = position;
            paneObservers.add(this);
            notifyObservers();
        }

        // When a postion is changed this function is called which updates each observer
        public void notifyObservers() {
            for (ObserverPane observer : paneObservers) {
                observer.updatePane(this.position);
            }
        }

        // change location
        @Override
        public void updatePane(int position) {
            System.out.println("position = " + position);
        }
    }
    /**
     * main functionality to the page. Creates the ui for the page
     *
     * @return overlay pane (base pane)
     */
    @FXML
    private StackPane createProjectPane (){
        CustomStackPane overLay = new CustomStackPane(0);
        overLay.setId("overlayID");
        overLay.setUserData(0); //0-2 0 = priority | 1 = inProgress | 2 = Complete
        VBox projectPane = createMainPane(overLay);
        HBox bigPane = createBigPane();
        projectPane.setId("projectPane");
        bigPane.setId("bigPane");
        //Rounding corners
        overLay.setStyle("-fx-background-radius: 20px; -fx-border-radius: 20px");
        projectPane.setStyle("-fx-background-radius: 20px; -fx-border-radius: 20px");
        bigPane.setStyle("-fx-background-radius: 20px; -fx-border-radius: 20px");
        projectList.add(overLay);

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

        for (StackPane pane : projectList) {
            Node temp = pane.getChildren().get(1);
            ((HBox) temp).setPrefSize(150, 150);
            pane.setPrefSize(150, 150);
            temp.setVisible(false);
            temp = pane.getChildren().get(0);
            temp.setVisible(true);
        }
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

        TextField projectResourcesField = new TextField();
        projectResourcesField.setPromptText("Enter list of required resources");

        TextField projectToolsField = new TextField();
        projectToolsField.setPromptText("Enter list of required tools");

        // Save Button
        Button saveButton = new Button("Save");
        saveButton.setOnAction(event -> {
            String projectName = projectNameField.getText();
            String projectDescription = projectDescriptionField.getText();

            if (!projectName.isEmpty() && !projectDescription.isEmpty()) {
                try {
                    // Call the backend function to create a new project and update the database
                    String result = FirebaseRequestHandler.CreateProject(projectName, projectDescription);

                    if (result.equals("success")) {
                        // Success case: disable editing and buttons
                        projectNameField.setEditable(false);
                        projectDescriptionField.setEditable(false);
                        projectResourcesField.setEditable(false);
                        projectToolsField.setEditable(false);
                        saveButton.setDisable(true);

                        // Optionally, show a success message
                        showSuccessAlert("Project Created", "Project was successfully saved to the database!");
                    } else {
                        // Handle the error returned by the backend
                        showErrorAlert("Error creating project", result);
                    }
                } catch (Exception e) {
                    // Handle exceptions from backend
                    showErrorAlert("Exception", "Failed to save the project: " + e.getMessage());
                }
            } else {
                // Show error if fields are empty
                showErrorAlert("Validation Error", "Project name and description cannot be empty.");
            }
        });


        // Edit Button
        Button editButton = new Button("Edit");
        editButton.setOnAction(event -> {
            projectNameField.setEditable(true);
            projectDescriptionField.setEditable(true);
            projectResourcesField.setEditable(true);
            projectToolsField.setEditable(true);
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
        projectPane.getChildren().addAll(projectNameField, projectDescriptionField, projectResourcesField, projectToolsField, buttonBox);

        // Highlight effect on hover
        projectPane.setOnMouseEntered(event -> {
            projectPane.setStyle("-fx-border-color: blue; -fx-border-width: 2;");
        });

        projectPane.setOnMouseExited(event -> {
            projectPane.setStyle("-fx-border-color: black; -fx-border-width: null;");
        });


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
                testTitle++;
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
        StackPane projectPan = createProjectPane();
        projectPan.setId("projectPanID");

        mainVbox.getChildren().add(projectPan);
        hideAllPanes(); // This for some reason fixes the size of panes.

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

