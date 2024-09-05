package firebase;

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


}
