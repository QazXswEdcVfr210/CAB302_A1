package firebase;

/*
############## THE HOLY GRAILS OF MAKING THIS THING WORK ##############
https://cloud.google.com/identity-platform/docs/reference/rest
https://firebase.google.com/docs/firestore/use-rest-api
https://firebase.google.com/docs/firestore/reference/rest/
######################################################################
*/

import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.http.json.JsonHttpContent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.util.Pair;

import com.qut.cab302_a1.models.Project;
import com.qut.cab302_a1.models.ProjectStep;

import org.apache.commons.lang3.RandomStringUtils;

/*
    This class handles making requests to Firebase through REST API requests.
    The purpose of this class is to abstract complex backend functionality into easy to use functions for frontend use.
 */

public class FirebaseRequestHandler {

    // Not a security risk as auth keys are distributed on user login
    private static final String FirebaseID = "AIzaSyA6q25fgqzmNdyO0jAYlWnSj259Aw7Dhr8";

    // Attempts login with provided credentials, if successful then returns true and stores UID and other user information.
    public static Boolean TryLogin(String email, String pass, Boolean bPrintResponse) throws Exception {
        try {
            // Set up request
            HttpTransport httpTransport = new NetHttpTransport();
            JsonFactory jsonFactory = new GsonFactory();
            String firebaseUrl = String.format("https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=%s", FirebaseID);

            // Create payload
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("email", email);
            requestBody.put("password", pass);
            requestBody.put("returnSecureToken", "true");

            // Make POST request
            HttpRequestFactory requestFactory = httpTransport.createRequestFactory();
            GenericUrl url = new GenericUrl(firebaseUrl);
            HttpContent content = new JsonHttpContent(jsonFactory, requestBody);
            HttpResponse response = requestFactory.buildPostRequest(url, content).execute();

            // Handle response (print response body to console if bPrintResponse is true.
            String responseBody = response.parseAsString();
            if(bPrintResponse && response.getStatusCode() == 200) {
                System.out.println(responseBody);
            }

            // Unpack the user's UID and email and save to FirebaseDataStorage, also save their ID and refresh tokens as well.
            FirebaseJSONUnpacker.ExtractBasicUserInformationFromAuth(responseBody, true);
            FirebaseJSONUnpacker.ExtractTokens(responseBody);

            // Get the user's project list
            GetProjectIds();
            GetProjects();

            return response.getStatusCode() == 200; // 200 response code means OK, everything else is treated as a login error

        } catch (HttpResponseException e) {
            // Print to console if anything goes wrong
            System.out.printf("Error signing in: %s%n", FirebaseJSONUnpacker.ExtractBadRequestErrorMessage(e.getContent()));
            return false;
        }
    }

    // Attempts to create an account with the provided credentials, if successful then returns true, if not then returns false and prints the error to the console.
    public static Boolean TrySignup(String email, String pass, String username, Boolean bPrintResponse) throws Exception {
        try {
            // Set up request
            HttpTransport httpTransport = new NetHttpTransport();
            JsonFactory jsonFactory = new GsonFactory();
            String firebaseUrl = String.format("https://identitytoolkit.googleapis.com/v1/accounts:signUp?key=%s", FirebaseID);

            // Create payload
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("email", email);
            requestBody.put("password", pass);
            requestBody.put("displayName", username);
            requestBody.put("returnSecureToken", "true");

            // Make POST request
            HttpRequestFactory requestFactory = httpTransport.createRequestFactory();
            GenericUrl url = new GenericUrl(firebaseUrl);
            HttpContent content = new JsonHttpContent(jsonFactory, requestBody);
            HttpResponse response = requestFactory.buildPostRequest(url, content).execute();

            // Handle response (print response body to console if bPrintResponse is true.
            if(bPrintResponse) {
                String responseBody = response.parseAsString();
                System.out.println(responseBody);
            }

            // Finish setting up user and attempt to login
            SetUpNewUser(username);
            TryLogin(email, pass, false);
            return true;

        } catch (HttpResponseException e){
            // 400 = bad request
            System.out.printf("Error creating account: %s%n", FirebaseJSONUnpacker.ExtractBadRequestErrorMessage(e.getContent()));
            return false;
        }

    }

    // Creates a new project, first returned string is either success or an error code, the second string is the project ID
    public static Pair<String, String> CreateProject(String projectName, String projectDescription) throws Exception {
        try {
            // Generate random project ID and check if project exists
            String projectID = "";
            boolean cont = true;
            while(cont) {
                projectID = RandomStringUtils.randomAlphabetic(16);
                cont = FirestoreHandler.CheckDocumentExists("Projects", projectID);
            }

            // Create payload
            Map<String, Object> requestBody = new HashMap<String, Object>();
            requestBody.put("projectName", Map.of("stringValue", projectName));                   // Add project name to payload
            requestBody.put("projectDescription", Map.of("stringValue", projectDescription));     // Add project description to payload
            requestBody.put("projectSteps", Map.of("mapValue", new HashMap<String, Object>()));   // Add project steps to payload
            requestBody.put("projectID", Map.of("stringValue", projectID));                       // Add project ID to payload

            // Create a new document with a randomly-generated projectID using the data provided in the projectFields payload
            Pair<Boolean, String> results = FirestoreHandler.CreateDocument("Projects", projectID, requestBody);

            // If the document was successfully created, add a reference to the project in the user's list of projectIDs
            if(results.getKey()) { AppendProjectToProfile(projectID);}
            return new Pair<String, String>("success", projectID);

        } catch (HttpResponseException e) {
            // Print to console if anything goes wrong
            return new Pair<String, String>(FirebaseJSONUnpacker.ExtractBadRequestErrorMessage(e.getContent()), null);
        }
    }

    // Modifies the name of a project
    public static Boolean UpdateProjectName(String projectID, String newName) throws Exception{

        Pair<Boolean, String> response;

        // Try to update the server's copy of the project
        try{
            // Create request body
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("projectName", Map.of(
                    "stringValue", newName
            ));

            // Send request
            response = FirestoreHandler.ModifyFieldValue("Projects", projectID, "projectName", requestBody);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        // If we were able to update the server's copy, update our local copy
        if(response.getKey()) {
            Pair<Project, Integer> projectIntegerPair = FirebaseDataStorage.getProjectByID(projectID);

            Project updatedProject = projectIntegerPair.getKey();
            updatedProject.setName(newName);

            FirebaseDataStorage.updateProject(updatedProject, projectIntegerPair.getValue());
        }

        return response.getKey();
    }

    // Modifies the description of a project
    public static Boolean UpdateProjectDescription(String projectID, String newDescription) throws Exception{

        Pair<Boolean, String> response;

        // Try to update the server's copy of the project
        try{
            // Create request body
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("projectDescription", Map.of(
                    "stringValue", newDescription
            ));

            // Send request
            response = FirestoreHandler.ModifyFieldValue("Projects", projectID, "projectDescription", requestBody);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        // If we were able to update the server's copy, update our local copy
        if(response.getKey()) {
            Pair<Project, Integer> projectIntegerPair = FirebaseDataStorage.getProjectByID(projectID);

            Project updatedProject = projectIntegerPair.getKey();
            updatedProject.setDescription(newDescription);

            FirebaseDataStorage.updateProject(updatedProject, projectIntegerPair.getValue());
        }

        return response.getKey();
    }

    // Deletes a project
    public static Boolean DeleteProject(String projectID) throws Exception {

        // Attempt to delete the document from Firestore
        Boolean projectWasDeleted;
        try {
            projectWasDeleted = FirestoreHandler.DeleteDocument("Projects", projectID).getKey();
            projectWasDeleted = true; // Return true if deletion was successful
        } catch (Exception e) {
            e.printStackTrace();
            projectWasDeleted = false; // Return false if an error occurred
        }

        // If we were able to delete the project from Firestore, delete its reference in Users/*uid*/projectIDs
        Boolean success = false;
        if(projectWasDeleted) {

            // Delete our local copy
            FirebaseDataStorage.deleteProject(projectID);

            // Create projectIDs array to send
            List<Map<String, String>> projects = new ArrayList<>();
            for(String p : FirebaseDataStorage.getProjectIDs()) {
                projects.add(Map.of("stringValue", p));
            }

            // Update request body and perform PATCH request
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("projectIDs", Map.of(
                    "arrayValue", Map.of(
                            "values", projects
                    )
            ));

            // Perform operation
            success = FirestoreHandler.ModifyFieldValue("Users", FirebaseDataStorage.getUid(), "projectIDs", requestBody).getKey();
        }

        return success;
    }

    // Creates a new project step and adds it to a project
    public static Boolean CreateProjectStep(String _projectID, String _projectStepName, String _projectStepDescription) throws Exception {
        try {
            // Get existing project steps
            Map<String, Object> stepData = new HashMap<>();
            Project project = FirebaseDataStorage.getProjectByID(_projectID).getKey();

            // Create step data for existing steps
            for(ProjectStep step : project.getProjectSteps()) {
                stepData.put(step.getName(), Map.of(
                        "mapValue", Map.of(
                                "fields", Map.of(
                                        "name", Map.of("stringValue", step.getName()),
                                        "desc", Map.of("stringValue", step.getDescription()),
                                        "isComplete", Map.of("booleanValue", step.getbIsCompleted())
                                )
                        )
                ));
            }

            // Create step data for new step
            stepData.put(_projectStepName, Map.of(
                    "mapValue", Map.of(
                            "fields", Map.of(
                                    "name", Map.of("stringValue", _projectStepName),
                                    "desc", Map.of("stringValue", _projectStepDescription),
                                    "isComplete", Map.of("booleanValue", false)
                            )
                    )
            ));

            // Create payload for PATCH request
            Map<String, Object> requestBody = Map.of(
                    "projectSteps", Map.of(
                            "mapValue", Map.of("fields", stepData)
                    )
            );

            // Create PATCH request to update document
            FirestoreHandler.ModifyFieldValue("Projects", _projectID, "projectSteps", requestBody);

            // Updates our copy of our projects
            GetProjects();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // Updates the isCompleted flag of a project step
    public static Boolean UpdateProjectStepCompleted(String projectID, String projectStepName, Boolean isCompleted) throws Exception {
        try {
            // Get projectSteps array from local data to minimise the number of calls to our db
            List<ProjectStep> projectSteps = FirebaseDataStorage.getProjectByID(projectID).getKey().getProjectSteps();

            // Select the relevant projectStep and edit the bIsCompleted variable
            for(ProjectStep step : projectSteps) {
                if(projectStepName.equals(step.getName())) {
                    step.setbIsCompleted(isCompleted);
                }
            }

            // Create step data
            Map<String, Object> stepData = new HashMap<>();
            for(ProjectStep step : projectSteps) {
                stepData.put(step.getName(), Map.of(
                        "mapValue", Map.of(
                                "fields", Map.of(
                                        "name", Map.of("stringValue", step.getName()),
                                        "desc", Map.of("stringValue", step.getDescription()),
                                        "isComplete", Map.of("booleanValue", step.getbIsCompleted())
                                )
                        )
                ));
            }

            // Create payload for PATCH request
            Map<String, Object> requestBody = Map.of(
                    "projectSteps", Map.of(
                            "mapValue", Map.of("fields", stepData)
                    )
            );

            // Update the database's copy of the projectSteps array
            FirestoreHandler.ModifyFieldValue("Projects", projectID, "projectSteps", requestBody);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Deletes a project step from a project
    public static Boolean DeleteProjectStep(String projectID, String projectStepName) throws Exception {
        try{
            FirestoreHandler.DeleteField("Projects", projectID, String.format("projectSteps.`%s`", projectStepName));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ######## these functions are used to abstract some of the above functions to improve readability ########

    // Gets the list of the current user's projects - called on login
    private static void GetProjectIds() throws Exception {
        try {
            // Get user document
            Pair<Boolean, String> result = FirestoreHandler.GetDocument("Users", FirebaseDataStorage.getUid());

            // Save list of project IDs to storage
            FirebaseJSONUnpacker.SaveUserProjectIDs(result.getValue());

        } catch (HttpResponseException e) {
            System.out.printf("Error getting user projects: %s%n", FirebaseJSONUnpacker.ExtractBadRequestErrorMessage(e.getContent()));
        }
    }

    // Gets all projects that the currently logged-in user owns and saves them to FirebaseDataStorage.projects - called on login
    private static Boolean GetProjects() throws Exception {
        List<String> projectIds = FirebaseDataStorage.getProjectIDs();

        if (projectIds == null || projectIds.isEmpty()) {
            System.out.println("No project IDs found or projectIds is null.");
            return false; // or handle accordingly
        }

        for (String projectID : projectIds) {
            try {
                Pair<Boolean, String> result = FirestoreHandler.GetDocument("Projects", projectID);
                if (result.getKey()) {
                    FirebaseJSONUnpacker.ExtractProjectInformation(result.getValue());
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }


    // Creates a new user document, then populates the new document with required fields.
    private static void SetUpNewUser(String username) throws Exception {
        try {
            // Create payload
            Map<String, Object> requestBody = new HashMap<String, Object>();
            requestBody.put("username", Map.of("stringValue", username));
            requestBody.put("projectIDs", Map.of("arrayValue", new HashMap<String, Object>()));

            FirestoreHandler.CreateDocument("Users", FirebaseDataStorage.getUid(), requestBody);

        } catch (HttpResponseException e) {
            System.out.printf("Error setting up new user: %s%n", FirebaseJSONUnpacker.ExtractBadRequestErrorMessage(e.getContent()));
        }
    }

    // Adds a reference to a newly-created project to the user's profile
    private static void AppendProjectToProfile(String projectID) throws Exception{
        // Create request body
        Map<String, Object> requestBody = new HashMap<>();

        // Get existing user projects
        List<Map<String, String>> projects = new ArrayList<>();
        for(String p : FirebaseDataStorage.getProjectIDs()) {
            projects.add(Map.of("stringValue", p));
        }

        // Add the new projectID to the map
        projects.add(Map.of("stringValue", projectID));

        // Update request body and perform PATCH request
        requestBody.put("projectIDs", Map.of(
                "arrayValue", Map.of(
                        "values", projects
                )
        ));
        FirestoreHandler.ModifyFieldValue("Users", FirebaseDataStorage.getUid(), "projectIDs", requestBody);

        // Update our projectID list by just appending to it instead of making another request
        FirebaseDataStorage.appendProjectID(projectID);
    }
}
