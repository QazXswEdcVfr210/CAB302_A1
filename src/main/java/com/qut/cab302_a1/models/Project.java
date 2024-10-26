package com.qut.cab302_a1.models;

// this is for testing until the real object is pulled from database.
public class Project {

    public String project;
    public String steps;
    public int stepsCount;
    public int stepsCompleted;
    public String[] users;
    public int status; //0 = ongoing, 4 complete etc format not yet decided
    public String description;
    public int tips;
    public int completedTips;

    public Project(String project, String steps, int stepsCount, int stepsCompleted, int tips, int completedTips, String[] users, int status, String description) {
        this.project = project;
        this.steps = steps;
        this.stepsCount = stepsCount;
        this.stepsCompleted = stepsCompleted;
        this.users = users;
        this.status = status;
        this.description = description;
        this.completedTips = completedTips;
        this.tips = tips;
    }



}
