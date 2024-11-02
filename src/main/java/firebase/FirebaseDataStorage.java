package firebase;

import com.qut.cab302_a1.models.Project;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

// This class stores commonly-accessed user data from Firebase so that we don't have to keep making requests.
public class FirebaseDataStorage {

    private static String uid;                                                                          // The User ID of the currently logged-in user.
    public static String getUid() { return uid; }                                                       // Getter
    public static void setUid(String uid) { FirebaseDataStorage.uid = uid; }                            // Setter

    private static String userEmail;                                                                    // The email address of the currently logged-in user.
    public static String getUserEmail() { return userEmail; }                                           // Getter
    public static void setUserEmail(String userEmail) { FirebaseDataStorage.userEmail = userEmail;}     // Setter

    private static Pair<String, String> userTokens;                                                                     // The user's ID and refresh tokens (key = ID, value = refresh).
    public static Pair<String, String> getUserTokens() { return userTokens; }                                           // Getter
    public static void setUserTokens(Pair<String, String> userTokens) { FirebaseDataStorage.userTokens = userTokens; }  // Setter

    private static List<String> projectIDs;                                                             // The user's list of projects by their ID.
    public static List<String> getProjectIDs() { return projectIDs; }                                   // Getter
    public static void setProjectIDs(List<String> _projectIDs){ projectIDs = _projectIDs; }             // Setter
    public static void appendProjectID(String projectID) { projectIDs.add(projectID); }                 // Append
    public static void deleteProjectID(String projectID) { projectIDs.remove(projectID); }              // Delete

    public static void clearProjectsData() {
        projectIDs = new ArrayList<>();
        projects = new ArrayList<>();
    }

    private static List<Project> projects = new ArrayList<>();                                          // The user's list of project instances
    public static List<Project> getProjects() {return FirebaseDataStorage.projects; }                   // Getter
    public static Pair<Project, Integer> getProjectByID(String projectID) {
        for(Project project : projects) {
            if(projectID.equals(project.getID())) {
                return new Pair<Project, Integer>(project, projects.indexOf(project));
            }
        }

        return new Pair<Project, Integer>(new Project("null", "null", "null", new ArrayList<>()), 0);
    }                         // Get by ID - returns project instance + index in projects list
    public static void appendProject(Project project) { projects.add(project); }                        // Append
    public static void updateProject(Project project, Integer index) { projects.set(index, project); }  // Update - effectively replaces the project at this.projects[index] with a new project
    public static void deleteProject(String projectID) {
        for(Project project : projects) {
            if(projectID.equals(project.getID())) {
                projectIDs.remove(project.getID());
                projects.remove(project);
                break;
            }
        }
    }                                            // Delete
}
