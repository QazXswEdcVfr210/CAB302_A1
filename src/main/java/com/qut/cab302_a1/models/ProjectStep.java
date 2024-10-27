package com.qut.cab302_a1.models;

public class ProjectStep {

    private String title;
    private String description;
    private Boolean bIsCompleted;

    private ProjectStep(String _title, String _description, Boolean _bIsCompleted) {
        this.title = _title;
        this.description = _description;
        this.bIsCompleted = _bIsCompleted;
    }

}
