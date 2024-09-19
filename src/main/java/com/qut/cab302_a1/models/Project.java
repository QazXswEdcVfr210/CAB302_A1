package com.qut.cab302_a1.models;

public class Project {

    private String project;
    private String steps;
    private int stepsCount;
    private int stepsCompleted;
    private String[] users;
    private int status; //0 = ongoing, 4 complete etc format not yet decided

    private Project(String project, String steps, int stepsCount, int stepsCompleted) {
        this.project = project;
        this.steps = steps;
        this.stepsCount = stepsCount;
        this.stepsCompleted = stepsCompleted;
    }

}
