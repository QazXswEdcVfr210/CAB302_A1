package com.qut.cab302_a1;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class LoginController {
    @FXML
    private Label loginText;

    @FXML
    protected void onLoginButtonClick() {
        loginText.setText("Login");
    }
}