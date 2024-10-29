package firebase;

import com.qut.cab302_a1.models.Project;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// This class stores commonly-accessed user data from Firebase so that we don't have to keep making requests.
public class FirebaseDataStorage {

    // The User ID of the currently logged-in user.
    private static String uid;
    // Getter
    public static String getUid() {
        return uid;
    }
    // Setter
    public static void setUid(String uid) {
        FirebaseDataStorage.uid = uid;
    }

    // The email address of the currently logged-in user.
    private static String userEmail;
    // Getter
    public static String getUserEmail() {
        return userEmail;
    }
    // Setter
    public static void setUserEmail(String userEmail) {
        FirebaseDataStorage.userEmail = userEmail;
    }

    // The user's ID and refresh tokens (key = ID, value = refresh).
    private static Pair<String, String> userTokens;
    // Getter
    public static Pair<String, String> getUserTokens() {
        return userTokens;
    }
    // Setter
    public static void setUserTokens(Pair<String, String> userTokens) {
        FirebaseDataStorage.userTokens = userTokens;
    }

    // The user's list of projects by their ID.
    private static List<String> projectIDs;
    // Getter
    public static List<String> getProjectIDs() {
        return FirebaseDataStorage.projectIDs;
    }
    // Setter
    public static void setProjectIDs(List<String> projectIDs){
        FirebaseDataStorage.projectIDs = projectIDs;
    }
    // Append
    public static void appendProjectID(String id) {
        FirebaseDataStorage.projectIDs.add(id);
    }

    // The user's list of project instances
    private static List<Project> projects = new ArrayList<>();
    // Getter
    public static List<Project> getProjects() {
        return FirebaseDataStorage.projects;
    }
    // Get by ID
    public static Project getProjectByID(String id) {
        for(Project project : projects) {
            if(id.equals(project.getID())) {
                return project;
            }
        }

        return new Project("null", "null", "null", new ArrayList<>());
    }
    // Append
    public static void appendProject(Project project) {
        projects.add(project);}

}
