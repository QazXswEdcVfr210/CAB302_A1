package com.qut.cab302_a1;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Popup;

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
    private boolean outsideClick(MouseEvent event, VBox box){
        if (!box.contains(event.getSceneX(), event.getSceneY())){
            System.out.println("Outside was clicked");
            return true;
        }
        return false;
    }

    @FXML
    private StackPane createProjectPane(){
        StackPane overLay = new StackPane();
        VBox projectPane = new VBox(20);
        VBox bigPane = new VBox(100);

        // small Pane stuff
        projectPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, null)));
        Label projectName = new Label("Project1");
        Label projectDescription = new Label("Description1");
        bigPane.setPrefSize(150, 150);

        // Lambda function that handles expanding the vbox when clicked.
        projectPane.setOnMouseClicked(actionEvent -> {
            //animation goes here

            final double scrollVal = mainScrollPane.getVvalue();
            System.out.println(scrollVal);
            bigPane.setVisible(true);
            projectPane.setVisible(false);
            bigPane.setPrefSize(450, 450);
            // Saves scroll vert height and applies it - System is a bit crappy.

            mainScrollPane.setVvalue(scrollVal);

        });

        projectPane.getChildren().addAll(projectName, projectDescription);
        projectName.setStyle("-fx-font-weight: bold");


        // bigPane stuff
        bigPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, null)));
        bigPane.setVisible(false);
        Label exampleLabel = new Label("Example");
        Button exitButton = new Button("Exit");
        Button materials = new Button("Materials");
        exitButton.setOnAction((ActionEvent event) -> {
            final double scrollVal = mainScrollPane.getVvalue();
            projectPane.setVisible(true);
            bigPane.setVisible(false);
            bigPane.setPrefSize(150, 150);
            mainScrollPane.setVvalue(scrollVal);
        });
        // Material menu
        Popup popupMenu = new Popup();
        AnchorPane popupPane = new AnchorPane();
        popupPane.setPrefSize(80, 80);
        Label testLabel = new Label("Test");
        popupMenu.getContent().add(testLabel);
        popupPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, null)));
        popupMenu.getContent().add(popupPane);
        popupMenu.hide();
        materials.setOnAction((ActionEvent event) -> {
            // button to get materials to show up.
            if (!popupMenu.isShowing()) {
                popupMenu.show(popupMenu, testLabel.getLayoutX(), testLabel.getLayoutY());

            }
            else{
                popupMenu.hide();
            }

        });

        bigPane.getChildren().addAll(exampleLabel, exitButton, materials);


        // stackPane stuff
        overLay.getChildren().addAll(projectPane, bigPane);

        return overLay;
    }


    @FXML
    protected void onCreatePanelAction(){

        System.out.println("Created Panel!");
    StackPane projectPan = createProjectPane();

    mainVbox.getChildren().add(projectPan);

    }
}
