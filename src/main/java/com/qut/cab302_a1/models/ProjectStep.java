package com.qut.cab302_a1.models;

public class ProjectStep {

    private String title;           // name of step
    private String description;     // description of step
    private Boolean bIsCompleted;   // if this step has been completed or not - can be used to calculate overall project completion

    private ProjectStep(String _title, String _description, Boolean _bIsCompleted) {
        this.title = _title;
        this.description = _description;
        this.bIsCompleted = _bIsCompleted;
    }

}
