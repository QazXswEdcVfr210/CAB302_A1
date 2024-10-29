package firebase;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.qut.cab302_a1.models.Project;
import com.qut.cab302_a1.models.ProjectStep;
import javafx.util.Pair;

import com.google.gson.JsonParser;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

// This is a class that unpacks JSON responses received from Firebase and extracts context-specific data.
public class FirebaseJSONUnpacker {

    // This extracts the UID and email address of the user from an authentication response. Used for user profile data.
    // Set bSaveToStorage if we want to save the extracted data to FirebaseDataStorage.
    public static Pair<String, String> ExtractBasicUserInformationFromAuth(String json, Boolean bSaveToStorage) throws Exception {
        try{
            // Google-recommended way to parse JSON
            JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();

            // Extract the things we need
            String uid = jsonObject.get("localId").getAsString();
            String email = jsonObject.get("email").getAsString();

            // Save to FirebaseDataStorage if bSaveToStorage is true.
            if(bSaveToStorage) {
                FirebaseDataStorage.setUid(uid);
                FirebaseDataStorage.setUserEmail(email);
            }

            return new Pair<>(uid, email);

        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Extracts the user's ID and refresh token to access Firestore.
    public static void ExtractTokens(String json) throws Exception {
        try{
            // Google-recommended way to parse JSON
            JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();

            // Extract the things we need
            String idToken = jsonObject.get("idToken").getAsString();
            String refreshToken = jsonObject.get("refreshToken").getAsString();
            Pair<String, String> tokens = new Pair<>(idToken, refreshToken);

            // Save to local storage
            FirebaseDataStorage.setUserTokens(tokens);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Extracts the error message from 400/bad request responses.
    public static String ExtractBadRequestErrorMessage(String json) throws Exception {
        try{
            // Google-recommended way to parse JSON
            JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();

            // Extract the things we need
            JsonObject error = jsonObject.get("error").getAsJsonObject();
            return error.get("message").getAsString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Extracts and saves user project IDs.
    public static void SaveUserProjectIDs(String json) throws Exception {
        try {
            // Google-recommended way to parse JSON
            JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();

            // Extract the things we need
            JsonArray projectIDsArray = jsonObject
                    .getAsJsonObject("fields")
                    .getAsJsonObject("projectIDs")
                    .getAsJsonObject("arrayValue")
                    .getAsJsonArray("values");

            // Store project ids
            List<String> projectIDs = new ArrayList<>();

            if(projectIDsArray != null && !projectIDsArray.isEmpty()) {
                for (JsonElement element : projectIDsArray) {
                    projectIDs.add(element.getAsJsonObject().get("stringValue").getAsString());
                }

            }

            // Save to local storage
            FirebaseDataStorage.setProjectIDs(projectIDs);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void ExtractProjectInformation(String json) throws Exception {
        try{
            // Google-recommended way to parse JSON
            JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject().getAsJsonObject("fields");

            // Extract project details
            String projectName = jsonObject.getAsJsonObject("projectName").get("stringValue").getAsString();
            String projectDescription = jsonObject.getAsJsonObject("projectDescription").get("stringValue").getAsString();

            // Store project steps
            List<ProjectStep> projectSteps = ExtractProjectStepsFromJson(jsonObject.getAsJsonObject("projectSteps").getAsJsonObject("mapValue").getAsJsonObject("fields"));

            // Create Project instance and append to project list
            Project project = new Project(projectName, projectDescription, projectSteps);
            FirebaseDataStorage.appendProject(project);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<ProjectStep> ExtractProjectStepsFromJson(JsonObject jsonObject) {
        List<ProjectStep> steps = new ArrayList<>();

        // Iterate over each step
        Set<String> keys = jsonObject.keySet();

        for(String key : keys) {
            // Get fields
            JsonObject fields = jsonObject.getAsJsonObject(key).getAsJsonObject("mapValue").getAsJsonObject("fields");

            // Extract information
            String stepName = fields.getAsJsonObject("name").get("stringValue").getAsString();
            String stepDesc = fields.getAsJsonObject("desc").get("stringValue").getAsString();
            Boolean stepComplete = fields.getAsJsonObject("isComplete").get("booleanValue").getAsBoolean();

            ProjectStep newStep = new ProjectStep(stepName, stepDesc, stepComplete);
            steps.add(newStep);
        }

        return steps;
    }
}
