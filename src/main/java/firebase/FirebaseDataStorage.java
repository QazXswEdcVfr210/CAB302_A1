package firebase;

import javafx.util.Pair;

import java.util.List;

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
}
