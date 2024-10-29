package com.qut.cab302_a1.models;

import java.util.List;

public class Project {

    private String project;
    private String steps;
    private int stepsCount;
    private int stepsCompleted;
    private String[] users;

    // this can be calculated as (number of completed steps / number of total steps)
    private int status; //0 = ongoing, 4 complete etc format not yet decided

    // List of the currently logged-in user's project steps
    private List<ProjectStep> projectSteps;

    private Project(String project, String steps, int stepsCount, int stepsCompleted, List<ProjectStep> projectSteps) {
        this.project = project;
        this.steps = steps;
        this.stepsCount = stepsCount;
        this.stepsCompleted = stepsCompleted;
        this.projectSteps = projectSteps;
    }

}
