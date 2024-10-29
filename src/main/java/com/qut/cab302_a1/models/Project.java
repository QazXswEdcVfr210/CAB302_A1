package com.qut.cab302_a1.models;

import java.util.List;

public class Project {

    public String id;
    private String name;
    private String description;
    private int stepsCount;

    // this can be calculated as (number of completed steps / number of total steps)
    private int status; //0 = ongoing, 4 complete etc format not yet decided

    // List of the currently logged-in user's project steps
    private List<ProjectStep> projectSteps;

    public Project(String id, String name, String description, List<ProjectStep> projectSteps) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.projectSteps = projectSteps;
        this.stepsCount = projectSteps.size();
    }

    // Dumps project info to console
    public void DebugProjectData() {
        System.out.printf("Project Data:\nID: %s%n\nName: %s%n\nDescription: %s%n\n Number of Steps: %s%n\n", this.id, this.name, this.description, this.stepsCount);

        for(ProjectStep step : projectSteps) {
            step.DebugProjectStepData();
        }
    }

    // Getters
    public String getID() {
        return this.id;
    }
    public String getName() {
        return this.name;
    }
    public List<ProjectStep> getProjectSteps() {
        return this.projectSteps;
    }

}
