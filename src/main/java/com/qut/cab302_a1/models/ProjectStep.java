package com.qut.cab302_a1.models;

public class ProjectStep {

    private String name;           // name of step
    private String description;     // description of step
    private Boolean bIsCompleted;   // if this step has been completed or not - can be used to calculate overall project completion

    public ProjectStep(String _name, String _description, Boolean _bIsCompleted) {
        this.name = _name;
        this.description = _description;
        this.bIsCompleted = _bIsCompleted;
    }

    // Dumps project step info to console
    public void DebugProjectStepData() {
        System.out.printf("Project Step Data:\nName: %s%n\nDescription: %s%n\nIs Complete: %s%n\n", this.name, this.description, this.bIsCompleted);
    }

    public void setCompleted(boolean state){
        if(state == true){
            this.bIsCompleted = true;
            //FirebaseRequestHandler.UpdateIsComplete(true);
        }
        else{
            this.bIsCompleted = false;
            //FirebaseRequestHandler.UpdateIsComplete(false);
        }
    }

    // Getters
    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public Boolean getbIsCompleted() {
        return this.bIsCompleted;
    }

    // Setters
    public void setbIsCompleted(Boolean _bIsCompleted) { this.bIsCompleted = _bIsCompleted; }

}
