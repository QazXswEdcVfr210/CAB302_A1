package com.qut.cab302_a1;

import javafx.fxml.FXML;

public class SelectedProjectController {

    private String project;
    private String steps;
    private int stepsCount;
    private int stepsCompleted;
    private String[] users;

    private SelectedProjectController(String project, String steps, int stepsCount, int stepsCompleted) {
        this.project = project;
        this.steps = steps;
        this.stepsCount = stepsCount;
        this.stepsCompleted = stepsCompleted;
    }

   private void setProject(String project){
        this.project = project;
   }
}
