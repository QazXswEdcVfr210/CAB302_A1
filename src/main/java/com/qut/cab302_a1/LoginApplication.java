package com.qut.cab302_a1;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginApplication extends Application {
    public static final int WIDTH = 400;
    public static final int HEIGHT = 500;
    public static final String TITLE = "Login";



    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginApplication.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), WIDTH, HEIGHT);
        stage.setTitle(TITLE);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

// TODO make bigPane exit out when clicked outside of pane
// add animation
// change bigPane to a stackPane and add material area.
// add styling