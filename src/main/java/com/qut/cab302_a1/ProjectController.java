package com.qut.cab302_a1;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class ProjectController {

    @FXML
    private VBox mainVbox;

    @FXML
    private VBox createProjectPane(){
        VBox projectPane = new VBox(20);
        projectPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, null)));
        Label projectName = new Label("Project1");
        Label projectDescription = new Label("Description1");
        projectPane.getChildren().addAll(projectName, projectDescription);
        projectName.setStyle("-fx-font-weight: bold");
        return projectPane;
    }

    @FXML
    private Button CreatePanel;

    @FXML
    protected void onCreatePanelAction(){

        System.out.println("Created Panel!");
    VBox projectPan = createProjectPane();
    mainVbox.getChildren().add(projectPan);

    }
}
