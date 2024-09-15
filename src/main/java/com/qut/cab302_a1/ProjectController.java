package com.qut.cab302_a1;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.util.Objects;
import java.util.Optional;
import java.util.ArrayList;
import java.util.List;

public class ProjectController {
    private List<StackPane> projectList = new ArrayList<>();

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


    /**
     * main functionality to the page. Creates the ui for the page
     *
     * @return overlay pane (base pane)
     */
    @FXML
    private StackPane createProjectPane(){
        StackPane overLay = new StackPane();
        overLay.setId("overlayID");
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
            Image placeholder = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/qut/cab302_a1/pictures/bob.jpg")));
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
                middlePane.setSpacing(220);
                Label title = new Label("Title");
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

                                StackPane progressPane = new StackPane(); // Change this bar to the future background colour and add black border
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
                TextArea textArea = getTextArea();

            rightSide.getChildren().addAll(middlePane, textArea);

        bigPane.getChildren().addAll(pictureBox, rightSide);
        bigPane.setMinSize(150, 150);
        return bigPane;
    }

    private static TextArea getTextArea() {
        TextArea textArea = new TextArea("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.");
        textArea.setId("textareaID");
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setMaxWidth(520.00);
        textArea.setMaxHeight(100.00);
        textArea.setStyle("-fx-background-color: transparent;");
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
    private int calculateProgress(int MAX_RANGE, int totalProgress, int currentProgress){
        if (totalProgress > MAX_RANGE || totalProgress == currentProgress || totalProgress == 0){
            return MAX_RANGE;
        }

        if (currentProgress != totalProgress && currentProgress == 0){
            return 0;
        }

        int range = MAX_RANGE / totalProgress;
        return range * currentProgress;
    }

    /**
     * Gets the color for the progress bar by comparing the size of
     * the bar compared to the full bar
     *
     * @param MAX_RANGE
     * @param progressRange
     * @return Color for progressBar
     */
    private Color pickColor(int MAX_RANGE, int progressRange){
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

    }

}

//TODO refactor into a more MVC style
// Give each pane a priority number for rerangement.

