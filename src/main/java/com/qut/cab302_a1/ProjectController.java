package com.qut.cab302_a1;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class ProjectController {

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

        VBox projectPane = new VBox(20);
        VBox bigPane = new VBox(100);

        // small Pane stuff
        projectPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, null)));
        Label projectName = new Label("Project1");
        Label projectDescription = new Label("Description1");
        Button newButton = new Button("Expand");
        newButton.setOnAction(actionEvent -> {
            //animation goes here

            bigPane.setVisible(true);
            projectPane.setVisible(false);
            bigPane.setPrefSize(300, 300);
            // Saves scroll horizontal height and applies it.
            Platform.runLater(()->{
                mainScrollPane.setHvalue(mainVbox.getHeight());
            });});


        projectPane.getChildren().addAll(projectName, projectDescription, newButton);
        projectName.setStyle("-fx-font-weight: bold");
        bigPane.setPrefSize(100, 100);


        // bigPane stuff
        bigPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, null)));
        bigPane.setVisible(false);
        Label exampleLabel = new Label("Example");
        bigPane.getChildren().addAll(exampleLabel);


        // stackPane stuff
        overLay.getChildren().addAll(projectPane, bigPane);

        return overLay;
    }

    @FXML
    private Button CreatePanel;

    @FXML
    protected void onCreatePanelAction(){

        System.out.println("Created Panel!");
    StackPane projectPan = createProjectPane();

    mainVbox.getChildren().add(projectPan);

    }
}
