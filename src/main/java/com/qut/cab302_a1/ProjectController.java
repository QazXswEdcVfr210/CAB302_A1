package com.qut.cab302_a1;



import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.w3c.dom.css.Rect;

import java.util.Optional;

import java.util.ArrayList;
import java.util.List;

public class ProjectController {
    private List<StackPane> projectList = new ArrayList<>();

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
        HBox bigPane = createBigPane();
        projectList.add(overLay);

        // Lambda function that handles expanding the vbox when clicked.
        projectPane.setOnMouseClicked(actionEvent -> {

            //animation goes here
            bigPane.setVisible(true);
            projectPane.setVisible(false);
            bigPane.setPrefSize(280, 280);
            bigPane.layout();

            // Gets the overlay height get the location scroll position
            double inScrollPanePos = overLay.getBoundsInParent().getMinY();
            double contentHeight = mainScrollPane.getContent().getBoundsInLocal().getHeight();
            double scrollPaneVal = inScrollPanePos / (contentHeight - mainScrollPane.getViewportBounds().getHeight());
            mainScrollPane.setVvalue(scrollPaneVal);
        });
//        //temp exit
//        Button exitButton = new Button("Exit");
//        exitButton.setOnAction((ActionEvent event) -> {
//            final double scrollVal = mainScrollPane.getVvalue();
//            projectPane.setVisible(true);
//            bigPane.setVisible(false);
//            bigPane.setPrefSize(150, 150);
//            mainScrollPane.setVvalue(scrollVal);
//        });

        bigPane.setOnMouseClicked(actionEvent -> {
            System.out.println("Test");
        });

        //bigPane.getChildren().addAll(exitButton);
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




    private HBox createBigPane(){
        HBox bigPane = new HBox(20);

        bigPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, null)));
        bigPane.setVisible(false);
        VBox pictureBox = new VBox(20);
        pictureBox.setMinWidth(190.00);
        // top, right?, left?, bottom?
        pictureBox.setPadding(new Insets(25, 0, 0, 25));
        Image placeholder = new Image(getClass().getResourceAsStream("/com/qut/cab302_a1/pictures/bob.jpg"));
        ImageView imageView = new ImageView(placeholder);
        imageView.setFitHeight(180);
        imageView.setFitWidth(180);
        imageView.setDisable(true);
        pictureBox.getChildren().addAll(imageView);
        //Right side
        VBox rightSide = new VBox(20);
        //Left side of rightSide panel
        rightSide.setPadding(new Insets(40, 0, 0, 0));
            HBox middlePane = new HBox(20);
            middlePane.setSpacing(220);
            Label title = new Label("Title");
                    // progressbar rightside of rightside
                    VBox progressBar = new VBox(20);
                        //top row
                        HBox progressBox = new HBox(20);
                        progressBox.setSpacing(185);
                        Label progressLabel = new Label("=  Progress");
                        Label tipsLabel = new Label("3/10");

                        progressBox.getChildren().addAll(progressLabel, tipsLabel);
                        Rectangle square = new Rectangle(270, 5);
                        square.setArcWidth(10);
                        square.setArcHeight(2);
                    progressBar.getChildren().addAll(progressBox, square);
            middlePane.getChildren().addAll(title, progressBar);
            TextArea textArea = new TextArea("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.");
            textArea.setEditable(false);
            textArea.setWrapText(true);
            textArea.setMaxWidth(520.00);
            textArea.setMaxHeight(100.00);
            //Hide background and scrollbar in css

        rightSide.getChildren().addAll(middlePane, textArea);

        bigPane.setMinSize(150, 150);
        bigPane.getChildren().addAll(pictureBox, rightSide);
        return bigPane;
    }


    @FXML
    protected void onCreatePanelAction(){
        System.out.println("Created Panel!");
        StackPane projectPan = createProjectPane();

        mainVbox.getChildren().add(projectPan);

    }
}

//TODO refactor into a more MVC style
