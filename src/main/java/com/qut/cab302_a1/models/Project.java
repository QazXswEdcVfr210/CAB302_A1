package com.qut.cab302_a1.models;

import java.util.List;

public class Project {

    private String name;
    private String description;
    private int stepsCount;

    // this can be calculated as (number of completed steps / number of total steps)
    private int status; //0 = ongoing, 4 complete etc format not yet decided

    // List of the currently logged-in user's project steps
    private List<ProjectStep> projectSteps;

    public Project(String name, String description, List<ProjectStep> projectSteps) {
        this.name = name;
        this.description = description;
        this.projectSteps = projectSteps;
        this.stepsCount = projectSteps.size();
    }

}
